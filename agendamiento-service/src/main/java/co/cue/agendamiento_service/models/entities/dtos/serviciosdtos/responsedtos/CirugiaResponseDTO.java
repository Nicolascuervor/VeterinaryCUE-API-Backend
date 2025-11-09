package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CirugiaResponseDTO extends ServicioResponseDTO {
    private boolean requiereQuirofano;
    private String notasPreoperatorias;
}
