package co.cue.agendamiento_service.models.entities.dtos;

import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DisponibilidadResponseDTO {
    private Long id;
    private Long veterinarioId;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private EstadoDisponibilidad estado;
    private Long citaId; // Será 'null' si está DISPONIBLE
}