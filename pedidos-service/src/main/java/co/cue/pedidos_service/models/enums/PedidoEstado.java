package co.cue.pedidos_service.models.enums;
/**
 * PedidoEstado
 *
 * Enum que representa los diferentes estados posibles de un pedido
 * dentro del flujo de compra. Permite controlar la lógica del ciclo
 * de vida del pedido desde su creación hasta su finalización.
 *
 * Estados:
 * - PENDIENTE: El pedido ha sido creado pero el pago aún no ha sido completado.
 * - COMPLETADO: El pago fue exitoso y el pedido se ha procesado correctamente.
 * - FALLIDO: Ocurrió un error durante el pago o el procesamiento del pedido.
 */
public enum PedidoEstado {
    /**
     * El pedido ha sido registrado pero aún está a la espera
     * de que el proceso de pago se complete.
     */
    PENDIENTE,
    /**
     * El pago se completó exitosamente y el pedido quedó finalizado.
     */
    COMPLETADO,
    /**
     * El pedido no pudo completarse por un error en el pago o
     * en la lógica interna de procesamiento.
     */
    FALLIDO
}
