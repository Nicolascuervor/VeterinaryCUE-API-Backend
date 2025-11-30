package co.cue.carrito_service.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO que representa un producto obtenido desde inventario-service
public class ProductoInventarioDTO {

    // ID del producto
    private Long id;

    // Nombre del producto
    private String nombre;

    // Precio del producto
    private Double precio;

    // Stock actual disponible
    private Integer stockActual;

    // Indica si el producto está disponible para venta
    private boolean disponibleParaVenta;
}