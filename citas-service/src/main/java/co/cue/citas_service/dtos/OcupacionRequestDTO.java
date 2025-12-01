package co.cue.citas_service.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OcupacionRequestDTO {
    private Long veterinarioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String tipo; // "CITA"
    private Long referenciaExternaId;
    private String observacion;
}
