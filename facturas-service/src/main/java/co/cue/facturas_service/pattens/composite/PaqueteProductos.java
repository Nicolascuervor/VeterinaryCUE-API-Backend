package co.cue.facturas_service.pattens.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


// Clase que representa un paquete compuesto de varios ítems de factura.
// Forma parte del patrón Composite implementando la interfaz ItemFactura.
public class PaqueteProductos implements ItemFactura {

    // Nombre del paquete o kit (ej: "Caja Mayorista").
    private String nombreKit;

    // Cantidad de paquetes que se venden juntos.
    private Integer cantidadKits;

    // Lista de componentes que forman este paquete.
    // Cada componente es también un ItemFactura (producto o sub-paquete).
    private List<ItemFactura> componentes = new ArrayList<>();


    // Constructor que inicializa el nombre del kit y la cantidad de kits.
    public PaqueteProductos(String nombreKit, Integer cantidadKits) {
        this.nombreKit = nombreKit;
        this.cantidadKits = cantidadKits;
    }

    // Agrega un ítem hijo (producto individual o paquete) al paquete compuesto.
    public void agregarComponente(ItemFactura item) {
        componentes.add(item);
    }

    @Override
    public BigDecimal calcularTotal() {
        // Suma el total de cada componente del paquete.
        BigDecimal totalComponentes = BigDecimal.ZERO;
        for (ItemFactura item : componentes) {
            totalComponentes = totalComponentes.add(item.calcularTotal());
        }
        // Nota: cantidadKits no multiplica el total porque el paquete actúa como un contenedor.
        // Si se desea multiplicar, debería hacerse aquí.
        return totalComponentes;
    }

    @Override
    public String generarResumen(int nivel) {
        // Construye un resumen textual del paquete y sus componentes.
        StringBuilder sb = new StringBuilder();

        // Indentación según el nivel de profundidad (para jerarquía visual).
        String indent = "  ".repeat(nivel);

        // Línea principal del paquete, mostrando nombre, cantidad y total calculado.
        sb.append(String.format("%s+ KIT: %s (x%d) [Total: $%s]%n",
                indent, nombreKit, cantidadKits, calcularTotal()));


        // Agrega el resumen de cada componente, aumentando la indentación.
        for (ItemFactura item : componentes) {
            sb.append(item.generarResumen(nivel + 1));
        }
        return sb.toString();
    }
}