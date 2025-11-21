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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioVenta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalLinea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_productos_id", nullable = false)
    private FacturaProductos factura;
}
