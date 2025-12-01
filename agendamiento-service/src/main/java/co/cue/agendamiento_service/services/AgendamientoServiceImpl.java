package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.mapper.AgendamientoMapper;
import co.cue.agendamiento_service.models.entities.JornadaLaboral;
import co.cue.agendamiento_service.models.entities.OcupacionAgenda;
import co.cue.agendamiento_service.models.entities.dtos.*;
import co.cue.agendamiento_service.models.entities.enums.TipoOcupacion;
import co.cue.agendamiento_service.repository.JornadaLaboralRepository;
import co.cue.agendamiento_service.repository.OcupacionAgendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Service
@RequiredArgsConstructor
public class AgendamientoServiceImpl implements IAgendamientoService {

    private final JornadaLaboralRepository jornadaRepository;
    private final OcupacionAgendaRepository ocupacionRepository;
    private final AgendamientoMapper mapper;

    private IAgendamientoService self;

    @Autowired
    public void setSelf(@Lazy IAgendamientoService self) {
        this.self = self;
    }

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
        // Usamos el mapper en lugar de un constructor manual
        return mapper.toJornadaResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public void validarDisponibilidad(Long veterinarioId, LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin) || inicio.isEqual(fin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        DayOfWeek diaSemana = inicio.getDayOfWeek();
        JornadaLaboral jornada = jornadaRepository.findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemana)
                .orElseThrow(() -> new IllegalStateException("El veterinario no trabaja los " + diaSemana));

        if (!jornada.isActiva()) {
            throw new IllegalStateException("El veterinario no está activo ese día.");
        }

        LocalTime horaInicioSolicitud = inicio.toLocalTime();
        LocalTime horaFinSolicitud = fin.toLocalTime();

        if (horaInicioSolicitud.isBefore(jornada.getHoraInicioJornada()) ||
                horaFinSolicitud.isAfter(jornada.getHoraFinJornada())) {
            throw new IllegalStateException("El horario solicitado está fuera de la jornada laboral.");
        }

        if (jornada.getHoraInicioDescanso() != null && jornada.getHoraFinDescanso() != null) {
            boolean chocaConDescanso =
                    (horaInicioSolicitud.isBefore(jornada.getHoraFinDescanso()) &&
                            horaFinSolicitud.isAfter(jornada.getHoraInicioDescanso()));

            if (chocaConDescanso) {
                throw new IllegalStateException("El horario solicitado choca con el tiempo de descanso.");
            }
        }

        List<OcupacionAgenda> conflictos = ocupacionRepository.findConflictos(veterinarioId, inicio, fin);

        if (!conflictos.isEmpty()) {
            throw new IllegalStateException("El veterinario ya tiene una ocupación en ese rango de tiempo.");
        }
    }

    @Override
    @Transactional
    public OcupacionResponseDTO crearOcupacion(OcupacionRequestDTO dto) {
        self.validarDisponibilidad(dto.getVeterinarioId(), dto.getFechaInicio(), dto.getFechaFin());
        OcupacionAgenda ocupacion = new OcupacionAgenda();
        ocupacion.setVeterinarioId(dto.getVeterinarioId());
        ocupacion.setFechaInicio(dto.getFechaInicio());
        ocupacion.setFechaFin(dto.getFechaFin());
        ocupacion.setTipo(dto.getTipo());
        ocupacion.setReferenciaExternaId(dto.getReferenciaExternaId());
        ocupacion.setObservacion(dto.getObservacion());

        OcupacionAgenda guardada = ocupacionRepository.save(ocupacion);

        // CORRECCIÓN 1: Usamos el Mapper aquí para evitar el error de constructor
        return mapper.toOcupacionResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendaVeterinarioDTO obtenerAgendaParaCalendario(Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        // 1. Traer el "Fondo" (Jornadas)
        List<JornadaLaboral> jornadas = jornadaRepository.findByVeterinarioIdAndActivaTrue(veterinarioId);

        // CORRECCIÓN 2: Convertimos la lista de entidades a DTOs usando Streams
        List<JornadaLaboralResponseDTO> jornadasDTO = jornadas.stream()
                .map(mapper::toJornadaResponseDTO)
                .toList();

        // 2. Traer las "Pinceladas" (Ocupaciones)
        List<OcupacionAgenda> ocupaciones = ocupacionRepository.findByVeterinarioIdAndFechaInicioBetween(
                veterinarioId,
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(LocalTime.MAX)
        );

        // CORRECCIÓN 3: Convertimos la lista de ocupaciones a DTOs
        List<OcupacionResponseDTO> ocupacionesDTO = ocupaciones.stream()
                .map(mapper::toOcupacionResponseDTO)
                .toList();

        // 3. Empaquetar y enviar usando el constructor de Lombok (@AllArgsConstructor)
        return new AgendaVeterinarioDTO(jornadasDTO, ocupacionesDTO);
    }

    @Override
    @Transactional
    public void eliminarOcupacionPorReferencia(Long referenciaId) {
        ocupacionRepository.deleteByReferenciaExternaIdAndTipo(referenciaId, TipoOcupacion.CITA);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JornadaLaboralResponseDTO> obtenerJornadasPorVeterinario(Long veterinarioId) {

        List<JornadaLaboral> jornadas = jornadaRepository.findAllByVeterinarioId(veterinarioId);

        return jornadas.stream()
                // MEJORA DE UX: Ordenar los días de la semana (Lunes a Domingo)
                .sorted((j1, j2) -> j1.getDiaSemana().compareTo(j2.getDiaSemana()))
                .map(mapper::toJornadaResponseDTO)
                .toList();
    }


    @Override
    @Transactional
    public void cambiarEstadoJornada(Long id, boolean activa) {
        JornadaLaboral jornada = jornadaRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Jornada no encontrada con ID: " + id));
        jornada.setActiva(activa);
        jornadaRepository.save(jornada);
    }
}