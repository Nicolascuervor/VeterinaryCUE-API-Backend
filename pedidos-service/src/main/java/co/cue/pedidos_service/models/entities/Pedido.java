package co.cue.pedidos_service.models.entities;

import co.cue.pedidos_service.models.enums.PedidoEstado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
/**
 * Entidad Pedido
 *
 * Representa un pedido realizado en el sistema. Esta entidad almacena
 * la información necesaria para procesar compras tanto de usuarios registrados
 * como de clientes invitados (guest).
 *
 * Contiene información del cliente, estado del pedido, monto total, relación
 * con los ítems solicitados y datos de pago (como el PaymentIntent de Stripe).
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {
    /**
     * Identificador único del pedido.
     * Se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * ID del usuario autenticado que realizó el pedido.
     * Puede ser null si el pedido fue realizado por un cliente invitado.
     */
    @Column(name = "usuario_id", nullable = true)
    private Long usuarioId;
    /**
     * ID de sesión del cliente invitado.
     * Se usa para identificar el carrito cuando el usuario no está autenticado.
     * Puede ser null si el pedido fue realizado por un usuario autenticado.
     */
    @Column(name = "session_id", length = 100, nullable = true)
    private String sessionId;
    /**
     * Nombre del cliente asociado al pedido.
     * Puede ser el nombre del usuario o el proporcionado por un cliente invitado.
     */

    @Column(name = "cliente_nombre", nullable = false)
    private String clienteNombre;
    /**
     * Correo electrónico del cliente asociado al pedido.
     * Usado para notificaciones y confirmaciones de compra.
     */
    @Column(name = "cliente_email", nullable = false)
    private String clienteEmail;
    /**
     * Estado actual del pedido.
     * Ejemplos: PENDIENTE, PAGADO, CANCELADO.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PedidoEstado estado = PedidoEstado.PENDIENTE;
    /**
     * Identificador del PaymentIntent generado por Stripe.
     * Este campo se completa durante el proceso de pago.
     */
    @Column(name = "stripe_payment_intent_id", length = 100)
    private String stripePaymentIntentId;
    /**
     * Monto total del pedido.
     * Incluye la suma de los precios de todos los productos multiplicados
     * por sus cantidades.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPedido;
    /**
     * Lista de ítems incluidos en el pedido.
     * Relación uno-a-muchos con PedidoItem.
     * - Cascade ALL: se guardan/eliminan automáticamente junto con el pedido.
     * - orphanRemoval: elimina ítems huérfanos.
     * - Lazy: se cargan solo cuando se necesitan.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PedidoItem> items = new HashSet<>();
    /**
     * Fecha y hora en que se creó el pedido.
     * Se genera automáticamente al momento de persistirlo.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
