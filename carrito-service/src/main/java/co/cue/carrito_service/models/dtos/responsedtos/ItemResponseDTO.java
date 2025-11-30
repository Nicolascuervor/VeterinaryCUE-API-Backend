package co.cue.carrito_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// DTO de respuesta para un item dentro del carrito
public class ItemResponseDTO {

    // ID del producto
    private Long productoId;

    // Cantidad del producto en el carrito
    private Integer cantidad;
}
