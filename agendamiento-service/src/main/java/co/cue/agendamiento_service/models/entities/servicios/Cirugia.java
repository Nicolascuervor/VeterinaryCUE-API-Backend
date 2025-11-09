package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios_cirugia")
@DiscriminatorValue("CIRUGIA")
@Getter
@Setter
@NoArgsConstructor
public class Cirugia extends Servicio {

    @Column(nullable = false)
    private boolean requiereQuirofano;

    @Column(length = 500)
    private String notasPreoperatorias;

    public Cirugia(String nombre, String descripcion, Integer duracion, BigDecimal precio, boolean requiereQuirofano, String notasPreoperatorias) {
        super(nombre, descripcion, duracion, precio);
        this.requiereQuirofano = requiereQuirofano;
        this.notasPreoperatorias = notasPreoperatorias;
    }
}