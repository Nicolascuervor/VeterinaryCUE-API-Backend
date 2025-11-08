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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_factura", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // (Mentor): Los campos base que propusiste
    @Column(nullable = false, unique = true)
    private String numFactura;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    private LocalDate fechaVencimiento; // Puede ser nulo si se paga de inmediato

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal impuestos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estadoFactura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "usuario_id", nullable = true) // Nulo si fue invitado
    private Long usuarioId;

    @Column(name = "id_origen", nullable = false)
    private Long idOrigen;

    @Column(name = "tipo_factura", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoFactura tipoFactura;
}
