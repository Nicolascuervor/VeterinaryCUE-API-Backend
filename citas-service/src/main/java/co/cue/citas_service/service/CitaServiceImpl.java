package co.cue.citas_service.service;

import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.citas_service.client.AgendamientoServiceClient;
import co.cue.citas_service.client.AuthServiceClient;
import co.cue.citas_service.client.MascotaServiceClient;
import co.cue.citas_service.dtos.*;
import co.cue.citas_service.dtos.enums.NotificationType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("citaServiceImpl")
@AllArgsConstructor
@Slf4j
public class CitaServiceImpl implements ICitaService {

    // Repositorio de citas
    private final CitaRepository citaRepository;

    // Cliente para comunicarse con el servicio de agendamiento
    private final AgendamientoServiceClient agendamientoClient;

    // Servicio para enviar eventos a Kafka
    private final KafkaProducerService kafkaProducer;

    // Mapper para convertir entre entidades y DTOs
    private final CitaMapper mapper;

    // Factory para obtener el comportamiento de cada estado
    private final CitaStateFactory stateFactory;

    private final AuthServiceClient authClient;       // Inyectar
    private final MascotaServiceClient mascotaClient; // Inyectar

    @Override
    @Transactional
    public CitaResponseDTO createCita(CitaRequestDTO dto, Long usuarioId) {
        log.info("Iniciando creación de cita para servicioId: {}", dto.getServicioId());

        // 1. Obtener datos del servicio (duración, precio)
        ServicioClienteDTO servicio = agendamientoClient.getServicioById(dto.getServicioId())
                .blockOptional()
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado: " + dto.getServicioId()));

        // 2. Calcular Fechas (El front ahora envía "fechaInicio", nosotros calculamos el fin)
        // NOTA: Debes actualizar CitaRequestDTO para recibir 'fechaInicio' (LocalDateTime) en lugar de 'idsDisponibilidad'.
        LocalDateTime fechaInicio = dto.getFechaInicio();
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }

        // Calculamos la hora fin sumando la duración del servicio
        LocalDateTime fechaFin = fechaInicio.plusMinutes(servicio.getDuracionPromedioMinutos());

        // 3. Crear entidad Cita (Pre-guardado para tener ID)
        Cita cita = new Cita();
        cita.setDuenioId(usuarioId);
        cita.setPetId(dto.getPetId());
        cita.setVeterinarianId(dto.getVeterinarianId());
        cita.setServicioId(servicio.getId());
        cita.setNombreServicio(servicio.getNombre());
        cita.setPrecioServicio(servicio.getPrecio());
        cita.setFechaHoraInicio(fechaInicio);
        cita.setFechaHoraFin(fechaFin);
        cita.setEstado(EstadoCita.CONFIRMADA); // O ESPERA, según tu flujo
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstadoGeneralMascota(dto.getEstadoGeneralMascota());

        // Guardamos primero para obtener el ID de la cita
        Cita citaGuardada = citaRepository.save(cita);

        // 4. Reservar en Agendamiento (Llamada al Microservicio)
        // Preparamos el DTO con el ID de la cita recién creada
        OcupacionRequestDTO reservaDTO = OcupacionRequestDTO.builder()
                .veterinarioId(dto.getVeterinarianId())
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .tipo("CITA") // Coincide con el Enum en el otro servicio
                .referenciaExternaId(citaGuardada.getId()) // ¡Clave para poder cancelar luego!
                .observacion("Cita para mascota ID: " + dto.getPetId())
                .build();

        try {
            // Llamada síncrona (block) para asegurar que la reserva sea exitosa antes de confirmar
            agendamientoClient.reservarEspacio(reservaDTO).block();
            log.info("Espacio reservado exitosamente en agendamiento para Cita ID: {}", citaGuardada.getId());
        } catch (Exception e) {
            log.error("Error al reservar espacio en agenda. Revirtiendo creación de cita.", e);
            // Si falla la reserva (ej: horario ocupado), borramos la cita local para mantener consistencia (Compensación simple)
            citaRepository.deleteById(citaGuardada.getId());
            throw new IllegalStateException("El horario seleccionado ya no está disponible o no es válido.", e);
        }

        try {
            enviarNotificacionConfirmacion(citaGuardada, usuarioId);
        } catch (Exception e) {
            log.warn("La cita se creó pero falló el envío de notificación: {}", e.getMessage());
        }

        return mapper.mapToResponseDTO(citaGuardada);
    }

    // Actualizar cita
    @Override
    @Transactional
    public CitaUpdateDTO updateCita(Long id, CitaUpdateDTO updateDTO) {
        log.info("Actualizando cita ID: {}", id);
        Cita cita = findCitaByIdPrivado(id);

        // Manejar transición de estado usando patrón State
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
        // Actualizar campos de la cita
        mapper.updateEntityFromDTO(updateDTO, cita);

        // Enviar evento si la cita se finalizó
        if (cita.getEstado() == EstadoCita.FINALIZADA) {
            log.info("Cita {} finalizada. Enviando evento a Kafka.", id);
            CitaCompletadaEventDTO evento = mapper.mapToCitaCompletadaEvent(cita);
            kafkaProducer.enviarCitaCompletada(evento);
        }
        // Guardar cambios
        Cita citaActualizada = citaRepository.save(cita);
        mapper.updateEntityFromDTO(updateDTO, citaActualizada);
        return updateDTO;
    }

    @Override
    @Transactional
    public void deleteCita(Long id) {
        log.warn("Iniciando cancelación de Cita ID: {}", id);

        // 1. Buscamos la cita localmente
        Cita cita = findCitaByIdPrivado(id);

        // 2. Cambiamos su estado a CANCELADA (No la borramos físicamente para historial)
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);

        // 3. Liberar el espacio en el microservicio de Agendamiento
        // Esto borrará la "cajita de color" (OcupacionAgenda) del calendario del veterinario.
        try {
            // Llamamos al nuevo endpoint '/interno/liberar/{referenciaId}'
            agendamientoClient.liberarEspacio(cita.getId()).block();
            log.info("Agenda liberada exitosamente para la Cita cancelada ID: {}", id);

        } catch (Exception e) {
            // Manejo de consistencia eventual:
            // Si el servicio de agendamiento falla, logueamos el error grave.
            // (En un sistema ideal, esto enviaría un evento a una cola "Dead Letter" para reintentar luego)
            log.error("¡ALERTA DE CONSISTENCIA! La cita {} se canceló localmente, pero falló la liberación de agenda en el servicio remoto. Error: {}", id, e.getMessage());

        }
    }

    // Buscar cita por ID y mapear a DTO
    @Override
    @Transactional(readOnly = true)
    public CitaResponseDTO findCitaById(Long id) {
        return citaRepository.findCitaById(id)
                .map(mapper::mapToResponseDTO) // <-- ¡Usando el Mapper!
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    // Buscar cita por ID (privado, devuelve entidad)
    private Cita findCitaByIdPrivado(Long id) {
        return citaRepository.findCitaById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    // Buscar citas por estado
    @Override
    @Transactional(readOnly = true)
    public List<Cita> findCitaByEstado(String estado) {
        EstadoCita estadoEnum = EstadoCita.valueOf(estado.toUpperCase());
        return citaRepository.findAllByEstado(estadoEnum); 
    }

    // Buscar citas de un día específico
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


    private void enviarNotificacionConfirmacion(Cita cita, Long usuarioId) {
        // 1. Obtener datos del Dueño (Síncrono/Bloqueante para simplificar el flujo)
        UsuarioClienteDTO usuario = authClient.obtenerUsuarioPorId(usuarioId).block();

        // 2. Obtener nombre de la Mascota (Best effort)
        String nombreMascota = "Tu Mascota";
        try {
            MascotaClienteDTO mascota = mascotaClient.findMascotaById(cita.getPetId()).block();
            if (mascota != null) {
                nombreMascota = "Mascota ID: " + cita.getPetId(); // Fallback si el DTO no tiene nombre
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener nombre de mascota para notificación");
        }

        if (usuario != null) {
            // 3. Construir Payload para el Email
            Map<String, String> payload = new HashMap<>();
            payload.put("correo", usuario.getCorreo());
            payload.put("nombreDuenio", usuario.getNombre() + " " + usuario.getApellido());
            payload.put("nombreMascota", nombreMascota);
            payload.put("fecha", cita.getFechaHoraInicio().toString().replace("T", " "));
            payload.put("medico", "Dr. ID " + cita.getVeterinarianId()); // Ideal: Buscar nombre del vet también

            // 4. Enviar evento
            NotificationRequestDTO notificacion = new NotificationRequestDTO(
                    NotificationType.CITA_CONFIRMACION,
                    payload
            );

            kafkaProducer.enviarNotificacion(notificacion);
            log.info("Solicitud de notificación enviada para Cita ID: {}", cita.getId());
        }
    }
}