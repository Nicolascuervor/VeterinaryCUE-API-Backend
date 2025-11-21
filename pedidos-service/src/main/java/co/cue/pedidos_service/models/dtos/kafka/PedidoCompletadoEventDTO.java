package co.cue.pedidos_service.models.dtos.kafka;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCompletadoEventDTO {
    private Long pedidoId;
    private Long usuarioId;
    private String clienteNombre;
    private String clienteEmail;
    private BigDecimal totalPedido;
    private LocalDateTime fechaCreacion;
    private Set<PedidoItemEventDTO> items;
}
