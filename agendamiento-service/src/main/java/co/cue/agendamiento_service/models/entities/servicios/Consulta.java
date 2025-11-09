package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios_consulta")
@DiscriminatorValue("CONSULTA")
@NoArgsConstructor
public class Consulta extends Servicio {
    public Consulta(String nombre, String descripcion, Integer duracion, BigDecimal precio) {
        super(nombre, descripcion, duracion, precio);
    }
}