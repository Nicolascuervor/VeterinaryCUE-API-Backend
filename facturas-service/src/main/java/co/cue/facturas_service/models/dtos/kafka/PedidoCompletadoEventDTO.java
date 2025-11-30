package co.cue.facturas_service.models.dtos.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

// Anotación de Lombok que genera automáticamente:
// getters, setters, toString(), equals(), hashCode() y constructor requerido.
@Data

// Genera un constructor vacío sin parámetros.
@NoArgsConstructor
public class PedidoCompletadoEventDTO {
    // Identificador único del pedido completado.
    private Long pedidoId;

    // Identificador del usuario que realizó el pedido.
    private Long usuarioId;

    // Nombre del cliente asociado al pedido.
    private String clienteNombre;

    // Correo electrónico del cliente, usado por otros microservicios
    // para notificaciones o facturación.
    private String clienteEmail;

    // Total final del pedido, incluyendo productos, servicios, impuestos, etc.
    private BigDecimal totalPedido;

    //  Fecha y hora en que el pedido fue creado originalmente.
    private LocalDateTime fechaCreacion;

    // Conjunto de ítems del pedido enviados dentro del evento,
    // para permitir que otros microservicios procesen los productos/servicios adquiridos.
    private Set<PedidoItemEventDTO> items;
}
