package co.cue.pedidos_service.models.dtos.kafka;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemEventDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
