package co.cue.pedidos_service.models.dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemEventDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario; // El precio snapshot que guardamos
}
