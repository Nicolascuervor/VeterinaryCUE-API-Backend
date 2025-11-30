package co.cue.facturas_service.models.enums;
// Enum que representa los métodos de pago disponibles para una factura.
public enum MetodoPago {
    // El cliente paga utilizando una tarjeta de crédito bancaria.
    TARJETA_CREDITO,

    // El pago se realiza directamente en efectivo, sin intermediarios electrónicos.
    EFECTIVO,

    // El cliente paga mediante transferencia bancaria o medios electrónicos equivalentes.
    TRANSFERENCIA
}