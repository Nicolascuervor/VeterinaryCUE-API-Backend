package co.cue.facturas_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facturas_productos")
// Valor que identifica en la jerarquía de herencia que esta es una factura de tipo PRODUCTOS.
@DiscriminatorValue("PRODUCTOS")
@Getter
@Setter
@NoArgsConstructor
public class FacturaProductos extends Factura {
    // Lista de líneas asociadas a la factura (productos comprados).
    // Relación uno-a-muchos: una factura puede tener múltiples líneas.
    // - mappedBy: indica que la relación está mapeada en la entidad LineaFactura (campo "factura").
    // - cascade: guarda/actualiza/elimina automáticamente las líneas junto con la factura.
    // - orphanRemoval: elimina de la BD las líneas que se remuevan de la factura.
    // - fetch LAZY: carga las líneas solo cuando se necesiten.
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<LineaFactura> lineas = new HashSet<>();
}
