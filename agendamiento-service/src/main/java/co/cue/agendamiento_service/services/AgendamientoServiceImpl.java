package co.cue.agendamiento_service.services;

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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgendamientoServiceImpl implements IAgendamientoService {

    private final JornadaLaboralRepository jornadaRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final AgendamientoMapper mapper; // (Mapper para JornadaDTO y DisponibilidadDTO)

    @Override
    @Transactional
    public JornadaLaboralResponseDTO crearActualizarJornada(JornadaLaboralRequestDTO dto) {
        // (Colega Senior): Lógica "UPSERT" (Update/Insert)
        JornadaLaboral jornada = jornadaRepository
                .findByVeterinarioIdAndDiaSemana(dto.getVeterinarioId(), dto.getDiaSemana())
                .orElse(new JornadaLaboral()); // Si no existe, crea una nueva

        // Actualiza los datos
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
    @Transactional
    public void generarSlotsDeDisponibilidad(Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin, int duracionSlot) {

        // 1. Obtener todas las plantillas (Lunes, Martes, etc.) para este vet
        List<JornadaLaboral> plantillas = jornadaRepository.findByVeterinarioIdAndActivaTrue(veterinarioId);
        if (plantillas.isEmpty()) {
            throw new EntityNotFoundException("No hay jornadas laborales activas configuradas para el veterinario ID: " + veterinarioId);
        }

        List<Disponibilidad> nuevosSlots = new ArrayList<>();

        // 2. Iterar por CADA DÍA en el rango (ej. 1-Dic al 31-Dic)
        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {

            // 3. Buscar la plantilla para ESE día de la semana
            DayOfWeek dia = fecha.getDayOfWeek();
            JornadaLaboral jornadaDelDia = plantillas.stream()
                    .filter(p -> p.getDiaSemana() == dia)
                    .findFirst().orElse(null);

            if (jornadaDelDia == null) {
                continue; // No trabaja este día, saltamos al siguiente.
            }

            // 4. (Arquitecto): Esta es la lógica de "Generador de Slots"
            LocalTime slotActual = jornadaDelDia.getHoraInicioJornada();
            LocalTime finJornada = jornadaDelDia.getHoraFinJornada();

            while (slotActual.isBefore(finJornada)) {
                LocalTime finSlot = slotActual.plusMinutes(duracionSlot);

                // Si el slot termina *después* del fin de la jornada, no se crea
                if (finSlot.isAfter(finJornada)) {
                    break;
                }

                // 5. Validar contra el descanso
                boolean estaEnDescanso = false;
                if (jornadaDelDia.getHoraInicioDescanso() != null) {
                    LocalTime inicioDescanso = jornadaDelDia.getHoraInicioDescanso();
                    LocalTime finDescanso = jornadaDelDia.getHoraFinDescanso();

                    // Si el slot (ej. 12:30-13:00) CHOCA con el descanso (12:00-13:00)
                    if (slotActual.isBefore(finDescanso) && finSlot.isAfter(inicioDescanso)) {
                        estaEnDescanso = true;
                    }
                }

                // 6. Si no está en descanso, ¡creamos el slot!
                if (!estaEnDescanso) {
                    LocalDateTime inicio = LocalDateTime.of(fecha, slotActual);
                    LocalDateTime fin = LocalDateTime.of(fecha, finSlot);

                    // (Nota: Aquí faltaría validar que el slot no exista ya)
                    nuevosSlots.add(new Disponibilidad(veterinarioId, inicio, fin));
                }

                // Avanzamos al siguiente slot
                slotActual = finSlot;
            }
        }

        // 7. Guardar todos los nuevos slots en la BD en una sola transacción
        disponibilidadRepository.saveAll(nuevosSlots);
        log.info("Se generaron {} nuevos slots para el vet ID {}", nuevosSlots.size(), veterinarioId);
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

        // Si todo salió bien, buscamos los slots actualizados para devolverlos
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
}
