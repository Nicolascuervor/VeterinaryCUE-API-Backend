package co.cue.citas_service.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DisponibilidadClienteDTO {

    // Id de la disponibilidad
    private Long id;

    // Id del veterinario asociado
    private Long veterinarioId;

    // Fecha y hora de inicio de la disponibilidad
    private LocalDateTime fechaHoraInicio;

    // Fecha y hora de fin de la disponibilidad
    private LocalDateTime fechaHoraFin;

    // Estado de la disponibilidad (por ejemplo: disponible, ocupada)
    private String estado;

    // Id de la cita asociada, si hay
    private Long citaId;
}
