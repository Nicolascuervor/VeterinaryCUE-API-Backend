package co.cue.facturas_service.strategies;

import co.cue.facturas_service.models.enums.TipoFactura;

public interface IFacturaGenerationStrategy {

    void generarFactura(Object eventPayload);
    TipoFactura getTipo();
}
