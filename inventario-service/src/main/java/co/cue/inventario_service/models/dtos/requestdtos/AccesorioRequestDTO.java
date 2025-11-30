package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccesorioRequestDTO extends ProductoRequestDTO {
    // Material con el que está fabricado el accesorio (cuero, tela, metal, etc.)
    private String material;

    // Tamaño del accesorio (pequeño, mediano, grande, etc.)
    private String tamanio;
}