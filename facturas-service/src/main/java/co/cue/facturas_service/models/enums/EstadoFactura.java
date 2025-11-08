package co.cue.facturas_service.models.enums;

public enum EstadoFactura {
    PENDIENTE_PAGO, // (Para facturas de Citas futuras)
    PAGADA,         // La que usaremos ahora
    ANULADA
}
