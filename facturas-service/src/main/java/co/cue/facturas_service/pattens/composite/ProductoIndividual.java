package co.cue.facturas_service.pattens.composite;

import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
public class ProductoIndividual implements ItemFactura {

    private String nombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;

    @Override
    public BigDecimal calcularTotal() {
        return precioUnitario.multiply(new BigDecimal(cantidad));
    }

    @Override
    public String generarResumen(int nivel) {
        String indent = "  ".repeat(nivel);
        return String.format("%s- %s (x%d): $%s%n", indent, nombre, cantidad, calcularTotal());
    }
}