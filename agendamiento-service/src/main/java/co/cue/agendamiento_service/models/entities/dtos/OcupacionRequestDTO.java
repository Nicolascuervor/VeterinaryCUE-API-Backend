package co.cue.agendamiento_service.models.entities.dtos;

import co.cue.agendamiento_service.models.entities.enums.TipoOcupacion;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OcupacionRequestDTO {
    private Long veterinarioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private TipoOcupacion tipo; // CITA, BLOQUEO_ADMIN, ETC.
    private Long referenciaExternaId; // ID de la Cita (si aplica)
    private String observacion;
}
