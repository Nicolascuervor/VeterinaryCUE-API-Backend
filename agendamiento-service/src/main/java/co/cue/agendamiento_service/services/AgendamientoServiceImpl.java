package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.mapper.AgendamientoMapper;
import co.cue.agendamiento_service.models.entities.Disponibilidad;
import co.cue.agendamiento_service.models.entities.JornadaLaboral;
import co.cue.agendamiento_service.models.entities.dtos.DisponibilidadResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import co.cue.agendamiento_service.repository.DisponibilidadRepository;
import co.cue.agendamiento_service.repository.JornadaLaboralRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AgendamientoServiceImpl implements IAgendamientoService {

    private final JornadaLaboralRepository jornadaRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final AgendamientoMapper mapper;

    @Override
    @Transactional
    public JornadaLaboralResponseDTO crearActualizarJornada(JornadaLaboralRequestDTO dto) {
        JornadaLaboral jornada = jornadaRepository
                .findByVeterinarioIdAndDiaSemana(dto.getVeterinarioId(), dto.getDiaSemana())
                .orElse(new JornadaLaboral());

        jornada.setVeterinarioId(dto.getVeterinarioId());
        jornada.setDiaSemana(dto.getDiaSemana());
        jornada.setHoraInicioJornada(dto.getHoraInicioJornada());
        jornada.setHoraFinJornada(dto.getHoraFinJornada());
        jornada.setHoraInicioDescanso(dto.getHoraInicioDescanso());
        jornada.setHoraFinDescanso(dto.getHoraFinDescanso());
        jornada.setActiva(true);
        JornadaLaboral guardada = jornadaRepository.save(jornada);
        return mapper.toJornadaResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisponibilidadResponseDTO> consultarDisponibilidadPorFecha(Long veterinarioId, LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay(); // 2025-11-10T00:00:00
        LocalDateTime finDia = fecha.atTime(LocalTime.MAX); // 2025-11-10T23:59:59

        List<Disponibilidad> slots = disponibilidadRepository
                .findByVeterinarioIdAndFechaHoraInicioBetweenAndEstado(
                        veterinarioId,
                        inicioDia,
                        finDia,
                        EstadoDisponibilidad.DISPONIBLE
                );

        return slots.stream()
                .map(mapper::toDisponibilidadResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DisponibilidadResponseDTO> reservarSlots(ReservaRequestDTO requestDTO) {

        // (Colega Senior): Usamos la consulta masiva que creamos en el Repo.
        int filasActualizadas = disponibilidadRepository.reservarSlotsMasivo(
                requestDTO.getIdsDisponibilidad(),
                requestDTO.getCitaId(),
                EstadoDisponibilidad.RESERVADO
        );

        // (Consultor): ¡Validación de Idempotencia!
        if (filasActualizadas != requestDTO.getIdsDisponibilidad().size()) {
            // Esto significa que alguien intentó reservar slots que YA estaban reservados
            // o no existían. La transacción se revierte.
            throw new IllegalStateException("Conflicto de reserva. Uno o más slots ya no estaban disponibles.");
        }

        List<Disponibilidad> slotsReservados = disponibilidadRepository.findAllById(requestDTO.getIdsDisponibilidad());
        return slotsReservados.stream()
                .map(mapper::toDisponibilidadResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void liberarSlotsPorCitaId(Long citaId) {
        List<Disponibilidad> slots = disponibilidadRepository.findByCitaId(citaId);
        if (slots.isEmpty()) {
            log.warn("Se intentó liberar slots para la citaId {}, pero no se encontró ninguno.", citaId);
            return;
        }

        for (Disponibilidad slot : slots) {
            slot.setEstado(EstadoDisponibilidad.DISPONIBLE);
            slot.setCitaId(null);
        }

        disponibilidadRepository.saveAll(slots);
        log.info("Se liberaron {} slots para la citaId {}", slots.size(), citaId);
    }

    @Override
    @Transactional
    public DisponibilidadResponseDTO actualizarEstadoSlotManualmente(Long disponibilidadId, EstadoDisponibilidad nuevoEstado) {
        if (nuevoEstado == EstadoDisponibilidad.RESERVADO) {
            throw new IllegalArgumentException("No se puede usar este método para RESERVAR. Use la API de /reservar.");
        }

        Disponibilidad slot = disponibilidadRepository.findById(disponibilidadId)
                .orElseThrow(() -> new EntityNotFoundException("Slot de disponibilidad no encontrado: " + disponibilidadId));

        slot.setEstado(nuevoEstado);
        slot.setCitaId(null); // Si se bloquea, se quita cualquier citaId (aunque no debería tener)

        Disponibilidad guardado = disponibilidadRepository.save(slot);
        return mapper.toDisponibilidadResponseDTO(guardado);
    }

    @Override
    @Transactional
    public void generarSlotsDeDisponibilidad(Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin, int duracionSlot) {
        List<JornadaLaboral> plantillas = jornadaRepository.findByVeterinarioIdAndActivaTrue(veterinarioId);
        if (plantillas.isEmpty()) {
            throw new EntityNotFoundException("No hay jornadas laborales activas configuradas para el veterinario ID: " + veterinarioId);
        }
        List<Disponibilidad> nuevosSlots = new ArrayList<>();
        Map<DayOfWeek, JornadaLaboral> mapaJornadas = plantillas.stream()
                .collect(Collectors.toMap(JornadaLaboral::getDiaSemana, jornada -> jornada));
        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
            JornadaLaboral jornadaDelDia = mapaJornadas.get(fecha.getDayOfWeek());
            if (jornadaDelDia == null) {
                continue;
            }
            List<Disponibilidad> slotsDelDia = crearSlotsParaJornada(jornadaDelDia, fecha, duracionSlot, veterinarioId);
            nuevosSlots.addAll(slotsDelDia);
        }
        disponibilidadRepository.saveAll(nuevosSlots);
        log.info("Se generaron {} nuevos slots para el vet ID {}", nuevosSlots.size(), veterinarioId);
    }

    private List<Disponibilidad> crearSlotsParaJornada(JornadaLaboral jornada, LocalDate fecha, int duracionSlot, Long veterinarioId) {
        List<Disponibilidad> slots = new ArrayList<>();
        LocalTime slotActual = jornada.getHoraInicioJornada();
        LocalTime finJornada = jornada.getHoraFinJornada();
        while (slotActual.isBefore(finJornada)) { // +1
            LocalTime finSlot = slotActual.plusMinutes(duracionSlot);
            if (finSlot.isAfter(finJornada)) { // +1 (anidado)
                break;
            }
            boolean estaEnDescanso = slotEstaEnDescanso(slotActual, finSlot, jornada);
            if (!estaEnDescanso) { // +1 (anidado)
                LocalDateTime inicio = LocalDateTime.of(fecha, slotActual);
                LocalDateTime fin = LocalDateTime.of(fecha, finSlot);
                slots.add(new Disponibilidad(veterinarioId, inicio, fin));
            }
            slotActual = finSlot;
        }
        return slots;
    }


    private boolean slotEstaEnDescanso(LocalTime slotInicio, LocalTime slotFin, JornadaLaboral jornada) {
        if (jornada.getHoraInicioDescanso() == null) { // +1
            return false;
        }
        LocalTime inicioDescanso = jornada.getHoraInicioDescanso();
        LocalTime finDescanso = jornada.getHoraFinDescanso();
        return  slotInicio.isBefore(finDescanso) && slotFin.isAfter(inicioDescanso);
    }




}
