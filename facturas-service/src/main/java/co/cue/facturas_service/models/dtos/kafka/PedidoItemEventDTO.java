package co.cue.facturas_service.models.dtos.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
// Anotación de Lombok que genera automáticamente:
// getters, setters, toString(), equals(), hashCode() y constructor requerido.
@Data

// Genera un constructor vacío sin parámetros.
@NoArgsConstructor
public class PedidoItemEventDTO {

    // Identificador único del producto asociado a este ítem del pedido.
    private Long productoId;

    // Cantidad del producto solicitado en el pedido.
    private Integer cantidad;

    // Precio unitario del producto al momento de realizar el pedido.
    // Se usa para calcular el subtotal correspondiente.
    private BigDecimal precioUnitario;
}
