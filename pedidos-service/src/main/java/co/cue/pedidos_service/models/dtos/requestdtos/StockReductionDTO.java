package co.cue.pedidos_service.models.dtos.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * StockReductionDTO
 *
 * DTO utilizado para representar una solicitud de reducción de stock
 * en el servicio de inventario. Este objeto se envía cuando un pedido es
 * confirmado y se debe descontar la cantidad correspondiente de cada producto.
 *
 * Propósito:
 * - Indicar qué producto debe disminuir su stock.
 * - Especificar cuántas unidades deben descontarse.
 *
 * Atributos:
 * ID del producto cuyo inventario será reducido.
 *  Cantidad a descontar del stock actual.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockReductionDTO {
    /**
     * Identificador único del producto al cual se aplicará la reducción de stock.
     */
    private Long productoId;
    /**
     * Cantidad de unidades que deben descontarse del inventario del producto.
     */
    private Integer cantidad;
}
