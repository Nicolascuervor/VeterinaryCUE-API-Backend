package co.cue.facturas_service.models.dtos.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PedidoItemEventDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
