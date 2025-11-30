package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la solicitud de reducción de stock.
 * Este objeto actúa como el contrato de datos para la comunicación interna entre el
 * microservicio de Pedidos y el de Inventario. Encapsula la información mínima necesaria
 * para decrementar las existencias de un producto específico tras una compra confirmada.
 * Su diseño simple y directo facilita la serialización a JSON en las llamadas REST
 * síncronas entre servicios.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockReductionDTO {

    /**
     * Identificador único del producto en el inventario cuyo stock debe ser reducido.
     * Debe corresponder a un ID válido y activo en la base de datos de inventario-service.
     */
    private Long productoId;

    /**
     * Cantidad de unidades a descontar del inventario
     * Representa el número de ítems comprados en el pedido. El servicio de inventario
     * utilizará este valor para realizar la resta aritmética sobre el 'stockActual'.
     * Debe ser un valor positivo mayor a cero.
     */
    private Integer cantidad;
}
