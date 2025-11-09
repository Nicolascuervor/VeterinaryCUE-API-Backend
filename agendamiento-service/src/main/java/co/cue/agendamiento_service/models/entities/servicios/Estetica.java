package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios_estetica")
@DiscriminatorValue("ESTETICA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estetica extends Servicio {

    @Column(length = 100)
    private String tipoArreglo;

    public Estetica(String nombre, String descripcion, Integer duracion, BigDecimal precio, String tipoArreglo) {
        super(nombre, descripcion, duracion, precio);
        this.tipoArreglo = tipoArreglo;
    }
}