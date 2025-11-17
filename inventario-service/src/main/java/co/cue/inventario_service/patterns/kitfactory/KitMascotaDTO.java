package co.cue.inventario_service.patterns.kitfactory;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KitMascotaDTO {
    private String nombreDelKit;
    private double descuentoPorcentual;
    private List<KitProductoDTO> componentes;
}
