package co.cue.citas_service.dto;

import co.cue.citas_service.entity.EstadoCita;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class CitaResponseDTO {
    private Long id;
    private Long petId;
    private Long veterinarianId;
    private LocalDate fechayhora;
    private String motivo;
    private String observaciones;
    private String estadoGeneral;
    private EstadoCita estado;
}
