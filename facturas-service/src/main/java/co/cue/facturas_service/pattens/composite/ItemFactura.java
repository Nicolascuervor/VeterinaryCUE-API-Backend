package co.cue.facturas_service.pattens.composite;

import java.math.BigDecimal;
// Interfaz base del patrón Composite para representar un ítem dentro de una factura.
// Puede ser implementada por productos individuales o por paquetes compuestos.
public interface ItemFactura {

    // Calcula el total del ítem.
    // En productos individuales: cantidad * precio.
    // En paquetes: suma de los totales de todos sus componentes.
    BigDecimal calcularTotal();

    // Genera un resumen descriptivo del ítem.
    // El parámetro "nivel" permite aplicar indentación para representar estructuras jerárquicas.
    // Ejemplo:
    // - Producto suelto -> sin indentación
    // - Producto dentro de un paquete -> indentación mayor
    String generarResumen(int nivel);
}
