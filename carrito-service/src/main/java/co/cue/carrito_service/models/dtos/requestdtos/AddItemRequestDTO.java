package co.cue.carrito_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO para agregar un item al carrito
public class AddItemRequestDTO {

    // ID del producto a agregar
    private Long productoId;

    // Cantidad del producto
    private Integer cantidad;
}