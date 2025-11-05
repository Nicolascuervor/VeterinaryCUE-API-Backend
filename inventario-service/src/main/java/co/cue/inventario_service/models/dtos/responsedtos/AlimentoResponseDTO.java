package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlimentoResponseDTO extends ProductoResponseDTO {
    private String tipoMascota;
    private Double pesoEnKg;
}
