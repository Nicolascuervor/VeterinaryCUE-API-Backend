package co.cue.facturas_service.pattens.composite;

import lombok.AllArgsConstructor;
import java.math.BigDecimal;
// Constructor automático generado por Lombok que crea un constructor con todos los argumentos.
@AllArgsConstructor

// Clase que representa un producto individual dentro de una factura.
// Implementa ItemFactura como parte del patrón Composite.
public class ProductoIndividual implements ItemFactura {

    private String nombre;   // Nombre del producto
    private Integer cantidad;  // Cantidad de unidades del producto.
    private BigDecimal precioUnitario;   // Precio unitario del producto.

    @Override
    // Calcula el total multiplicando cantidad * precio unitario.
    public BigDecimal calcularTotal() {
        return precioUnitario.multiply(new BigDecimal(cantidad));
    }

    @Override
    public String generarResumen(int nivel) {
        // Indentación según el nivel de jerarquía (para estructura del Composite).
        String indent = "  ".repeat(nivel);

        // Devuelve una línea con el resumen del producto:
        return String.format("%s- %s (x%d): $%s%n", indent, nombre, cantidad, calcularTotal());
    }
}