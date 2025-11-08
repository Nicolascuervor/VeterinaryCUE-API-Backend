package co.cue.carrito_service.models.dtos;

import lombok.Getter;
import lombok.Setter;

// Este DTO Mapea la respuesta que esperamos de inventario-service
@Getter
@Setter
public class ProductoInventarioDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stockActual;
    private boolean disponibleParaVenta;
}