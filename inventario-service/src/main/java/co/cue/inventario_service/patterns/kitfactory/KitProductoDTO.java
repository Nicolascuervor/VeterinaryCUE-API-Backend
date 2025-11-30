package co.cue.inventario_service.patterns.kitfactory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KitProductoDTO {

    // Nombre del producto que forma parte del kit
    private String nombreProducto;

    // Cantidad de unidades de este producto dentro del kit
    private int cantidad;
}
