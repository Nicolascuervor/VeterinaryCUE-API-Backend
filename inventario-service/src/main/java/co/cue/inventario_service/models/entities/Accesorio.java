package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos_accesorio") // Nombre de la tabla para accesorios
@DiscriminatorValue("ACCESORIO")  // Identificador del tipo de producto en la herencia
@Getter
@Setter
public class Accesorio extends Producto {

    // Material del accesorio (ej: plástico, metal, tela)
    private String material;

    // Tamaño del accesorio (ej: pequeño, mediano, grande)
    private String tamanio;
}