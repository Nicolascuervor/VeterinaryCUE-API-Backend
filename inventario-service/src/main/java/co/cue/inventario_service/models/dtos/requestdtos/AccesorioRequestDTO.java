package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccesorioRequestDTO extends ProductoRequestDTO {
    private String material;
    private String tamanio;
}