package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccesorioResponseDTO extends ProductoResponseDTO {
    private String material;
    private String tamanio;
}
