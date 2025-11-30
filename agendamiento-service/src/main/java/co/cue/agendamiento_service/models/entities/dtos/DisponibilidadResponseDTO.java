package co.cue.agendamiento_service.models.entities.dtos;

import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter   // Genera automáticamente los métodos get para todos los campos
@Setter    // Genera automáticamente los métodos set para todos los campos
public class DisponibilidadResponseDTO {     // DTO de respuesta para información de disponibilidad de un slot
    private Long id;                         // Identificador único del slot de disponibilidad
    private Long veterinarioId;              // ID del veterinario asociado al slot
    private LocalDateTime fechaHoraInicio;   // Fecha y hora de inicio del slot
    private LocalDateTime fechaHoraFin;      // Fecha y hora de fin del slot
    private EstadoDisponibilidad estado;     // Estado actual del slot (disponible, reservado, etc.)
    private Long citaId;                     // ID de la cita asociada al slot (si está reservado)
}