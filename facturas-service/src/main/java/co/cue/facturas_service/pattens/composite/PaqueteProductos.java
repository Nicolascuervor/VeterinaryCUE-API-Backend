package co.cue.facturas_service.pattens.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaqueteProductos implements ItemFactura {

    private String nombreKit;
    private Integer cantidadKits;
    private List<ItemFactura> componentes = new ArrayList<>();

    public PaqueteProductos(String nombreKit, Integer cantidadKits) {
        this.nombreKit = nombreKit;
        this.cantidadKits = cantidadKits;
    }

    public void agregarComponente(ItemFactura item) {
        componentes.add(item);
    }

    @Override
    public BigDecimal calcularTotal() {
        BigDecimal totalComponentes = BigDecimal.ZERO;
        for (ItemFactura item : componentes) {
            totalComponentes = totalComponentes.add(item.calcularTotal());
        }
        return totalComponentes;
    }

    @Override
    public String generarResumen(int nivel) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(nivel);

        sb.append(String.format("%s+ KIT: %s (x%d) [Total: $%s]\n",
                indent, nombreKit, cantidadKits, calcularTotal()));

        for (ItemFactura item : componentes) {
            sb.append(item.generarResumen(nivel + 1)); // Recursividad real
        }
        return sb.toString();
    }
}