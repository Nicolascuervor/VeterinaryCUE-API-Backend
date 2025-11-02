package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos_accesorio")
@DiscriminatorValue("ACCESORIO")
public class Accesorio extends Producto {

    private String material;
    private String tamanio;
}