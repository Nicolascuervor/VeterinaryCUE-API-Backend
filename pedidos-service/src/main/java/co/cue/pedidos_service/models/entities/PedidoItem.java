package co.cue.pedidos_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
/**
 * Entidad PedidoItem
 *
 * Representa un ítem individual dentro de un pedido. Cada registro contiene
 * información del producto comprado, la cantidad solicitada y los valores
 * económicos asociados al cálculo del subtotal.
 *
 * Esta entidad forma parte de una relación muchos-a-uno con la entidad Pedido.
 */
@Entity
@Table(name = "pedidos_items")
@Getter
@Setter
@NoArgsConstructor
public class PedidoItem {
    /**
     * Identificador único del ítem dentro del pedido.
     * Generado automáticamente en la base de datos.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * ID del producto al que pertenece este ítem.
     * Se almacena directamente para evitar una dependencia fuerte con el microservicio de inventario.
     */
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    /**
     * Cantidad de unidades del producto incluidas en este ítem del pedido.
     */
    @Column(nullable = false)
    private Integer cantidad;
    /**
     * Precio unitario del producto al momento del pedido.
     * Se guarda para mantener consistencia histórica incluso si el precio cambia luego.
     */
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    /**
     * Subtotal de esta línea dentro del pedido.
     * Calculado como: cantidad * precioUnitario.
     */

    @Column(name = "subtotal_linea", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalLinea;
    /**
     * Referencia al pedido al cual pertenece este ítem.
     * Relación muchos-a-uno con la entidad Pedido.
     * Carga Lazy para evitar traer el pedido completo cuando no es necesario.
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}