package co.cue.inventario_service.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos_medicina")
@DiscriminatorValue("MEDICINA")
@Getter
@Setter
public class Medicina extends Producto {

    // Describe la composición química o ingredientes del medicamento
    @Column(length = 500)
    private String composicion;

    // Indica la dosis recomendada para su uso
    private String dosisRecomendada;
}