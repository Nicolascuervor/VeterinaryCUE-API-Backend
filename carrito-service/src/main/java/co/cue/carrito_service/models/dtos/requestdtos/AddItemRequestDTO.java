package co.cue.carrito_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

// DTO simple para agregar un item.
// No necesitamos validaciones de 'stock' aquí, solo los datos crudos.
@Getter
@Setter
public class AddItemRequestDTO {
    private Long productoId;
    private Integer cantidad;
}