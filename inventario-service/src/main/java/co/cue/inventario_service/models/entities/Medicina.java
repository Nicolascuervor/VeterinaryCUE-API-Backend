package co.cue.inventario_service.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos_medicina")
@DiscriminatorValue("MEDICINA")
public class Medicina extends Producto {

    @Column(length = 500)
    private String composicion;
    private String dosisRecomendada;
}