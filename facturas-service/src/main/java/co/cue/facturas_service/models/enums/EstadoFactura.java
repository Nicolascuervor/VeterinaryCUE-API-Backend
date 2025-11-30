package co.cue.facturas_service.models.enums;
// Enum que representa los estados posibles en los que puede estar una factura.
public enum EstadoFactura {
    // La factura fue generada pero aún no ha sido pagada.
    PENDIENTE_PAGO,

    // La factura ya fue cancelada completamente por el usuario.
    PAGADA,

    // La factura fue anulada, ya sea por error, devolución o acción administrativa.
    ANULADA
}
