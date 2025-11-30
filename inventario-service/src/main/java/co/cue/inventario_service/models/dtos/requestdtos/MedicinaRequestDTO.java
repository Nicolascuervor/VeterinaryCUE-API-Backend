package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicinaRequestDTO extends ProductoRequestDTO {

    // Componentes o fórmula de la medicina
    private String composicion;

    // Dosis recomendada para el uso del producto
    private String dosisRecomendada;
}
