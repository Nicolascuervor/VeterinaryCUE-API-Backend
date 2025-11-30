package co.cue.facturas_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "facturas_lineas_productos")
@Getter
@Setter
@NoArgsConstructor
public class LineaFactura {
    // Identificador único de la línea dentro de la factura.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del producto asociado a esta línea.
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    // Cantidad del producto comprada.
    @Column(nullable = false)
    private Integer cantidad;

    // Precio unitario del producto al momento de la venta.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioVenta;

    // Subtotal calculado: cantidad * precio unitario.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalLinea;

    // Relación muchos-a-uno:
    // Muchas líneas pertenecen a una sola factura de productos.
    // fetch LAZY → solo carga la factura cuando se necesite.
    // JoinColumn indica la llave foránea en la tabla.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_productos_id", nullable = false)
    private FacturaProductos factura;
}
