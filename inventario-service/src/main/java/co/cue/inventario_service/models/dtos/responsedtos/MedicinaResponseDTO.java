package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicinaResponseDTO extends ProductoResponseDTO {
    private String composicion;
    private String dosisRecomendada;
}
