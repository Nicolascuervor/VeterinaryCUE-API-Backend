package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos_alimento")  // Tabla específica para alimentos
@DiscriminatorValue("ALIMENTO")  // Identificador dentro de la herencia
@Getter
@Setter
public class Alimento extends Producto {
    // Indica para qué tipo de mascota es el alimento (perro, gato, etc.)
    private String tipoMascota;

    // Peso del alimento en kilogramos
    private Double pesoEnKg;
}
