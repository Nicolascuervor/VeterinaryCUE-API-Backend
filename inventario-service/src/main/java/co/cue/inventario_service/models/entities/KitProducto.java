package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos_kit")
@DiscriminatorValue("KIT")
@Getter
@Setter
public class KitProducto extends Producto {
    public KitProducto() {
        super(); // Llama al constructor de Producto

        // Indicamos que este producto es un kit
        this.setEsKit(true);
    }
}