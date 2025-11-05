package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos_accesorio")
@DiscriminatorValue("ACCESORIO")
@Getter
@Setter
public class Accesorio extends Producto {
    private String material;
    private String tamanio;
}