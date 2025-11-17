package co.cue.inventario_service.patterns.kitfactory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KitProductoDTO {
    private String nombreProducto;
    private int cantidad;
}
