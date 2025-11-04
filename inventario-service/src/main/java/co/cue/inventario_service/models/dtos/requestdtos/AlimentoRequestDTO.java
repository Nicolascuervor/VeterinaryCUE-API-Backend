package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlimentoRequestDTO extends ProductoRequestDTO {
    private String tipoMascota;
    private Double pesoEnKg;
}
