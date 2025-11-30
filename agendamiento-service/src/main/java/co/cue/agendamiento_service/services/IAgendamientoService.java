package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.models.entities.dtos.DisponibilidadResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;

import java.time.LocalDate;
import java.util.List;

public interface IAgendamientoService {
    // Crea o actualiza la jornada laboral de un veterinario para un día específico
    // Recibe un DTO con la información de la jornada y devuelve la jornada guardada como DTO de respuesta
    JornadaLaboralResponseDTO crearActualizarJornada(JornadaLaboralRequestDTO requestDTO);

    // Genera slots de disponibilidad para un veterinario en un rango de fechas y con duración específica
    // Se basa en las jornadas laborales activas configuradas para ese veterinario
    void generarSlotsDeDisponibilidad(Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin, int duracionSlot);

    // Consulta la disponibilidad de un veterinario en un día específico
    // Devuelve una lista de DTOs con los slots que están DISPONIBLES
    List<DisponibilidadResponseDTO> consultarDisponibilidadPorFecha(Long veterinarioId, LocalDate fecha);

    // Reserva uno o varios slots de disponibilidad asociados a una cita
    // Recibe un DTO con la lista de IDs de slots y el ID de la cita, devuelve los slots reservados como DTOs
    List<DisponibilidadResponseDTO> reservarSlots(ReservaRequestDTO requestDTO);

    // Libera todos los slots que estaban reservados para una cita específica
    // Cambia su estado a DISPONIBLE y elimina la referencia a la cita
    void liberarSlotsPorCitaId(Long citaId);

    // Permite actualizar manualmente el estado de un slot
    // No se debe usar para reservar (solo DISPONIBLE o BLOQUEADO)
    DisponibilidadResponseDTO actualizarEstadoSlotManualmente(Long disponibilidadId, EstadoDisponibilidad nuevoEstado);

    // Obtiene la información de varios slots por su lista de IDs
    // Devuelve la lista de DTOs correspondientes
    List<DisponibilidadResponseDTO> findDisponibilidadByIds(List<Long> ids);

    // Busca los IDs de los veterinarios que ofrecen un servicio específico
    // Útil para filtrar disponibilidad por servicio
    List<Long> findVeterinarioIdsByServicioId(Long servicioId);
}
