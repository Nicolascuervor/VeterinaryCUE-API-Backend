package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.models.entities.dtos.DisponibilidadResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;

import java.time.LocalDate;
import java.util.List;

public interface IAgendamientoService {

    JornadaLaboralResponseDTO crearActualizarJornada(JornadaLaboralRequestDTO requestDTO);

    void generarSlotsDeDisponibilidad(Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin, int duracionSlot);

    List<DisponibilidadResponseDTO> consultarDisponibilidadPorFecha(Long veterinarioId, LocalDate fecha);

    List<DisponibilidadResponseDTO> reservarSlots(ReservaRequestDTO requestDTO);

    void liberarSlotsPorCitaId(Long citaId);

    DisponibilidadResponseDTO actualizarEstadoSlotManualmente(Long disponibilidadId, EstadoDisponibilidad nuevoEstado);

    List<DisponibilidadResponseDTO> findDisponibilidadByIds(List<Long> ids);

    List<Long> findVeterinarioIdsByServicioId(Long servicioId);
}
