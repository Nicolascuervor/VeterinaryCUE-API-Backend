package co.cue.citas_service.service;

import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.citas_service.client.AgendamientoServiceClient;
import co.cue.citas_service.dtos.*;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.entity.EstadoCita;
import co.cue.citas_service.events.CitaCompletadaEventDTO;
import co.cue.citas_service.mapper.CitaMapper;
import co.cue.citas_service.pattern.state.CitaStateFactory;
import co.cue.citas_service.pattern.state.ICitaState;
import co.cue.citas_service.repository.CitaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;


@Service("citaServiceImpl") // <-- (Arquitecto): Le damos un nombre único
@AllArgsConstructor
@Slf4j
public class CitaServiceImpl implements ICitaService {

    private final CitaRepository citaRepository;
    private final AgendamientoServiceClient agendamientoClient;
    private final KafkaProducerService kafkaProducer;
    private final CitaMapper mapper;
    private final CitaStateFactory stateFactory;

    @Override
    @Transactional
    public CitaResponseDTO createCita(CitaRequestDTO dto, Long usuarioId) {
        log.info("Iniciando creación de cita para servicioId: {}", dto.getServicioId());

        // 1. Obtener datos de los otros servicios
        ServicioClienteDTO servicio = agendamientoClient.getServicioById(dto.getServicioId())
                .blockOptional()
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado: " + dto.getServicioId()));
        List<DisponibilidadClienteDTO> slots = agendamientoClient.getDisponibilidadByIds(dto.getIdsDisponibilidad())
                .blockOptional()
                .orElseThrow(() -> new EntityNotFoundException("Slots no encontrados: " + dto.getIdsDisponibilidad()));

        // 2. Validar Lógica de Negocio
        validarConsistenciaDeSlots(slots, servicio, dto.getVeterinarianId());
        slots.sort(Comparator.comparing(DisponibilidadClienteDTO::getFechaHoraInicio));
        LocalDateTime fechaHoraInicio = slots.get(0).getFechaHoraInicio();
        LocalDateTime fechaHoraFin = slots.get(slots.size() - 1).getFechaHoraFin();

        // 3. Crear Entidad Cita
        Cita cita = new Cita();
        cita.setDuenioId(usuarioId);
        cita.setPetId(dto.getPetId());
        cita.setVeterinarianId(dto.getVeterinarianId());
        cita.setServicioId(servicio.getId());
        cita.setNombreServicio(servicio.getNombre());
        cita.setPrecioServicio(servicio.getPrecio());
        cita.setFechaHoraInicio(fechaHoraInicio);
        cita.setFechaHoraFin(fechaHoraFin);
        cita.setEstado(EstadoCita.CONFIRMADA);
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstadoGeneralMascota(dto.getEstadoGeneralMascota());

        // 4. Guardar Cita
        Cita citaGuardada = citaRepository.save(cita);

        // 5. Reservar Slots
        ReservaRequestDTO reservaDTO = new ReservaRequestDTO();
        reservaDTO.setCitaId(citaGuardada.getId());
        reservaDTO.setIdsDisponibilidad(dto.getIdsDisponibilidad());

        try {
            agendamientoClient.reservarSlots(reservaDTO).block();
            log.info("Slots reservados exitosamente para Cita ID: {}", citaGuardada.getId());
        } catch (Exception e) {
            log.error("Error al reservar slots. Revirtiendo creación de cita.", e);
            throw new IllegalStateException("No se pudieron reservar los slots. Intente de nuevo.", e);
        }

        return mapper.mapToResponseDTO(citaGuardada);
    }

    private void validarConsistenciaDeSlots(List<DisponibilidadClienteDTO> slots, ServicioClienteDTO servicio, Long vetIdRequest) {
        if (slots == null || slots.isEmpty()) {
            throw new IllegalArgumentException("La lista de slots no puede estar vacía.");
        }
        for (DisponibilidadClienteDTO slot : slots) {
            if (!slot.getVeterinarioId().equals(vetIdRequest)) {
                throw new IllegalArgumentException("Todos los slots deben pertenecer al veterinario solicitado.");
            }
            if (!"DISPONIBLE".equals(slot.getEstado())) {
                throw new IllegalStateException("El slot " + slot.getId() + " ya no está disponible.");
            }
        }
        int duracionSlot = 30;
        int slotsNecesarios = servicio.getDuracionPromedioMinutos() / duracionSlot;
        if (slots.size() != slotsNecesarios) {
            throw new IllegalArgumentException("Número de slots incorrecto. El servicio requiere " +
                    servicio.getDuracionPromedioMinutos() + " min (" + slotsNecesarios + " slots), pero se enviaron " + slots.size());
        }
        slots.sort(Comparator.comparing(DisponibilidadClienteDTO::getFechaHoraInicio));
        for (int i = 0; i < slots.size() - 1; i++) {
            LocalDateTime finSlotActual = slots.get(i).getFechaHoraFin();
            LocalDateTime inicioSlotSiguiente = slots.get(i+1).getFechaHoraInicio();
            if (!finSlotActual.equals(inicioSlotSiguiente)) {
                throw new IllegalArgumentException("Los slots seleccionados no son consecutivos.");
            }
        }
        log.info("Validación de slots exitosa.");
    }


    @Override
    @Transactional
    public CitaUpdateDTO updateCita(Long id, CitaUpdateDTO updateDTO) {
        log.info("Actualizando cita ID: {}", id);
        Cita cita = findCitaByIdPrivado(id);
        if (updateDTO.getEstado() != null && updateDTO.getEstado() != cita.getEstado()) {
            log.debug("Intentando transición de estado: {} -> {}", cita.getEstado(), updateDTO.getEstado());
            ICitaState comportamientoEstadoActual = stateFactory.getState(cita.getEstado());
            switch (updateDTO.getEstado()) {
                case CONFIRMADA -> comportamientoEstadoActual.confirmar(cita);
                case EN_PROGRESO -> comportamientoEstadoActual.iniciar(cita);
                case FINALIZADA -> comportamientoEstadoActual.finalizar(cita);
                case CANCELADA -> comportamientoEstadoActual.cancelar(cita);
                case NO_ASISTIO -> comportamientoEstadoActual.noAsistio(cita);
                default -> throw new IllegalArgumentException("Transición no soportada hacia: " + updateDTO.getEstado());
            }
        }
        mapper.updateEntityFromDTO(updateDTO, cita);
        if (cita.getEstado() == EstadoCita.FINALIZADA) {
            log.info("Cita {} finalizada. Enviando evento a Kafka.", id);
            CitaCompletadaEventDTO evento = mapper.mapToCitaCompletadaEvent(cita);
            kafkaProducer.enviarCitaCompletada(evento);
        }
        // Guardamos cambios
        Cita citaActualizada = citaRepository.save(cita);
        mapper.updateEntityFromDTO(updateDTO, citaActualizada);
        return updateDTO;
    }

    @Override
    @Transactional
    public void deleteCita(Long id) {
        log.warn("Cancelando Cita ID: {}", id);
        Cita cita = findCitaByIdPrivado(id);

        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);

        try {
            agendamientoClient.liberarSlots(cita.getId()).block();
            log.info("Slots liberados para Cita cancelada ID: {}", id);
        } catch (Exception e) {
            log.error("¡ERROR CRÍTICO! No se pudieron liberar los slots para la Cita ID: {}", id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponseDTO findCitaById(Long id) {
        return citaRepository.findCitaById(id)
                .map(mapper::mapToResponseDTO) // <-- ¡Usando el Mapper!
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    private Cita findCitaByIdPrivado(Long id) {
        return citaRepository.findCitaById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> findCitaByEstado(String estado) {
        EstadoCita estadoEnum = EstadoCita.valueOf(estado.toUpperCase());
        return citaRepository.findAllByEstado(estadoEnum); 
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> findCitasDelDia(LocalDate fecha) {
        log.info("Buscando citas para la fecha: {}", fecha);

        LocalDateTime inicioDelDia = fecha.atStartOfDay();

        LocalDateTime finDelDia = fecha.atTime(LocalTime.MAX);

        List<Cita> citasDelDia = citaRepository.findAllByFechaHoraInicioBetween(
                inicioDelDia,
                finDelDia
        );
        return citasDelDia.stream()
                .map(mapper::mapToResponseDTO)
                .toList();
    }
}