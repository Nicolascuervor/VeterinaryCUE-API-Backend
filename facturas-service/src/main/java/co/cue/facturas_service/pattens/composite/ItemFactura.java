package co.cue.facturas_service.pattens.composite;

import java.math.BigDecimal;

public interface ItemFactura {
    BigDecimal calcularTotal();
    String generarResumen(int nivel);
}
