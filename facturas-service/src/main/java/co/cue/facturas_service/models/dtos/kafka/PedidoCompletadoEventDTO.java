package co.cue.facturas_service.models.dtos.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@NoArgsConstructor
public class PedidoCompletadoEventDTO {
    private Long pedidoId;
    private Long usuarioId;
    private String clienteNombre;
    private String clienteEmail;
    private BigDecimal totalPedido;
    private LocalDateTime fechaCreacion;
    private Set<PedidoItemEventDTO> items;
}
