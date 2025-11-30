package co.cue.pedidos_service.models.dtos.kafka;

import lombok.*;

import java.math.BigDecimal;
/**
 * PedidoItemEventDTO
 *
 * DTO utilizado dentro del evento de pedido completado para representar
 * cada uno de los ítems que forman parte del pedido.
 *
 * Propósito:
 * - Transportar información detallada del producto comprado.
 * - Ser parte del evento publicado hacia otros microservicios.
 *
 * Atributos:
 * Identificador del producto comprado.
 *  Cantidad adquirida del producto.
 *  Precio unitario del producto al momento de la compra.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemEventDTO {
    /**
     * ID único del producto que fue comprado.
     */
    private Long productoId;
    /**
     * Cantidad del producto incluida en el pedido.
     */
    private Integer cantidad;
    /**
     * Precio unitario del producto al momento de realizar el pedido.
     * Importante para mantener la integridad histórica del valor cobrado.
     */
    private BigDecimal precioUnitario;
}
