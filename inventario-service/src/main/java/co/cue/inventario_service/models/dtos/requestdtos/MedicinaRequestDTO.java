package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicinaRequestDTO extends ProductoRequestDTO {
    private String composicion;
    private String dosisRecomendada;
}
