package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;

import java.time.LocalDate;


import co.cue.agendamiento_service.models.entities.dtos.*;

import java.time.LocalDateTime;
import java.util.List;

public interface IAgendamientoService {

    // Configuración Base (Se mantiene igual)
    JornadaLaboralResponseDTO crearActualizarJornada(JornadaLaboralRequestDTO requestDTO);

    // --- NUEVOS MÉTODOS ---

    /**
     * Valida matemáticamente si un rango de tiempo es viable.
     * @throws IllegalStateException si hay conflicto o está fuera de horario.
     */
    void validarDisponibilidad(Long veterinarioId, LocalDateTime inicio, LocalDateTime fin);

    /**
     * Crea un bloqueo en la agenda (Cita o Administrativo).
     * Internamente llama a validarDisponibilidad().
     */
    OcupacionResponseDTO crearOcupacion(OcupacionRequestDTO ocupacionDTO);

    /**
     * Método para el Calendario Visual.
     * Retorna la configuración + ocupaciones en un rango de fechas.
     */
    AgendaVeterinarioDTO obtenerAgendaParaCalendario(Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Libera un espacio (ej: al cancelar una cita).
     */
    void eliminarOcupacionPorReferencia(Long referenciaId);

    List<JornadaLaboralResponseDTO> obtenerJornadasPorVeterinario(Long veterinarioId);

    /**
     * Cambia el estado (Activo/Inactivo) de una jornada laboral específica.
     */
    void cambiarEstadoJornada(Long id, boolean activa);

    List<JornadaLaboralResponseDTO> crearJornadasMasivas(JornadaMasivaRequestDTO dto);
}
