package co.cue.pedidos_service.models.dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCompletadoEventDTO {
    private Long pedidoId;
    private Long usuarioId; // Nulo si fue invitado
    private String clienteNombre;
    private String clienteEmail;
    private BigDecimal totalPedido;
    private LocalDateTime fechaCreacion;
    private Set<PedidoItemEventDTO> items;
}
