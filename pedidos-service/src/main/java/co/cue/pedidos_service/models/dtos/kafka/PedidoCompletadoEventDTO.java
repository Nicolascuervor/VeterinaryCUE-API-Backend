package co.cue.pedidos_service.models.dtos.kafka;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
/**
 * PedidoCompletadoEventDTO
 *
 * DTO utilizado para representar un evento de pedido completado.
 * Este objeto se emplea típicamente en comunicación interna entre microservicios,
 * mensajería/event-driven architecture o procesos asíncronos tras finalizar un pedido.
 *
 * Propósito:
 * - Transportar información completa del pedido después de haber sido procesado con éxito.
 * - Notificar a otros servicios (notificaciones, inventario, facturación, análisis, etc.).
 *
 * Uso típico:
 * - Publicación de eventos en colas o brokers (Kafka, RabbitMQ, etc.).
 * - Flujo post-pago o post-confirmación.
 *
 * Atributos:
 *  ID único del pedido completado.
 *  ID del usuario que realizó la compra (puede ser null para invitados).
 *  Nombre completo del cliente que realizó el pedido.
 * Correo del cliente, útil para envío de comprobantes o notificaciones.
 *  Monto total del pedido calculado al finalizar el proceso.
 *  Fecha y hora en que se generó el pedido.
 *  Conjunto de ítems incluidos en el pedido, representados como eventos.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCompletadoEventDTO {
    /**
     * Identificador único del pedido completado.
     */
    private Long pedidoId;

    /**
     * Identificador del usuario que realizó el pedido (puede ser null si es un cliente invitado).
     */
    private Long usuarioId;
    /**
     * Nombre del cliente asociado al pedido.
     */

    private String clienteNombre;
    /**
     * Correo electrónico del cliente que realizó la compra.
     */
    private String clienteEmail;
    /**
     * Valor total del pedido finalizado.
     */
    private BigDecimal totalPedido;
    /**
     * Fecha y hora de creación del pedido.
     */
    private LocalDateTime fechaCreacion;
    /**
     * Lista de ítems que componen el pedido en el evento.
     */
    private Set<PedidoItemEventDTO> items;
}
