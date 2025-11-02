package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos_alimento")
@DiscriminatorValue("ALIMENTO") // <-- La etiqueta que va en 'tipo_producto'
public class Alimento extends Producto {

    private String tipoMascota;
    private Double pesoEnKg;
}
