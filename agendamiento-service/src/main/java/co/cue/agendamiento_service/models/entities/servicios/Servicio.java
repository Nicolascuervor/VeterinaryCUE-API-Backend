package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;


@Entity
@Table(name = "servicios")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_servicio")
@Getter
@Setter
@NoArgsConstructor
public abstract class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Integer duracionPromedioMinutos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean activo = true;

    protected Servicio(String nombre, String descripcion, Integer duracionPromedioMinutos, BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionPromedioMinutos = duracionPromedioMinutos;
        this.precio = precio;
        this.activo = true;
    }
}
