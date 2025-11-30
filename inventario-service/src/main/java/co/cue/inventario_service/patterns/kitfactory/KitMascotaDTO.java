package co.cue.inventario_service.patterns.kitfactory;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KitMascotaDTO {

    // Nombre del kit (ej: "Kit de Bienvenida para Perros")
    private String nombreDelKit;

    // Porcentaje de descuento que se aplica al precio total del kit
    private double descuentoPorcentual;

    // Lista de productos que componen el kit
    private List<KitProductoDTO> componentes;
}
