package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CirugiaRequestDTO extends  ServicioRequestDTO {
    private boolean requiereQuirofano;
    private String notasPreoperatorias;

}