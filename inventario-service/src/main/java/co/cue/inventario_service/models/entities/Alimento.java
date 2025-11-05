package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos_alimento")
@DiscriminatorValue("ALIMENTO")
@Getter
@Setter
public class Alimento extends Producto {
    private String tipoMascota;
    private Double pesoEnKg;
}
