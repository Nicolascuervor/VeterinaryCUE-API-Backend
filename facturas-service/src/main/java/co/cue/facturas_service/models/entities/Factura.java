package co.cue.facturas_service.models.entities;

import co.cue.facturas_service.models.enums.EstadoFactura;
import co.cue.facturas_service.models.enums.MetodoPago;
import co.cue.facturas_service.models.enums.TipoFactura;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
// Estrategia de herencia donde cada subclase tendrá su propia tabla enlazada con la tabla base.
@Inheritance(strategy = InheritanceType.JOINED)

// Columna usada por JPA para diferenciar el tipo de factura dentro de la jerarquía.
@DiscriminatorColumn(name = "tipo_factura", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class Factura {

    // Identificador único de la factura, generado automáticamente por la BD.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número único asignado a la factura. No puede repetirse.
    @Column(nullable = false, unique = true)
    private String numFactura;


    // Fecha en la que se emitió la factura.
    @Column(nullable = false)
    private LocalDate fechaEmision;


    // Fecha límite para pagar la factura; puede ser nula si no aplica.
    private LocalDate fechaVencimiento;


    // Valor total antes de aplicar impuestos.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    // Monto de los impuestos aplicados a la factura.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal impuestos;

    // Total final de la factura (subtotal + impuestos).
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;


    // Estado actual de la factura (PENDIENTE, PAGADA, CANCELADA, etc.).
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estadoFactura;

    // Método usado para pagar la factura (efectivo, tarjeta, transferencia, etc.).
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    // Identificador del usuario asociado a la factura. Puede ser nulo.
    @Column(name = "usuario_id", nullable = true)
    private Long usuarioId;


    // Identificador de la entidad de origen que generó esta factura (por ejemplo, un pedido).
    @Column(name = "id_origen", nullable = false)
    private Long idOrigen;


    // Tipo de factura definido por la herencia. Lo llena automáticamente JPA.
    @Column(name = "tipo_factura", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoFactura tipoFactura;
}
