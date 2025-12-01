package co.cue.citas_service.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OcupacionResponseDTO {
    private Long id;
    private Long veterinarioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String tipo; // Puede ser String o Enum, dependiendo si quieres compartir el Enum
    private Long referenciaExternaId;
    private String observacion;
}
