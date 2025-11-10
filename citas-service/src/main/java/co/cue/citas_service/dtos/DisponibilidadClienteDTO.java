package co.cue.citas_service.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DisponibilidadClienteDTO {
    private Long id;
    private Long veterinarioId;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String estado;
    private Long citaId;
}
