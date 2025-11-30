package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoriaRequestDTO {

    // Nombre de la categoría
    private String nombre;

    // Descripción de la categoría
    private String descripcion;
}
