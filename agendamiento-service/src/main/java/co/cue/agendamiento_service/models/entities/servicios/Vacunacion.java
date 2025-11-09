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
@Table(name = "servicios_vacunacion")
@DiscriminatorValue("VACUNACION")
@Getter
@Setter
@NoArgsConstructor
public class Vacunacion extends Servicio {
    @Column(length = 100)
    private String nombreBiologico;
    public Vacunacion(String nombre, String descripcion, Integer duracion, BigDecimal precio, String nombreBiologico) {
        super(nombre, descripcion, duracion, precio);
        this.nombreBiologico = nombreBiologico;
    }
}
