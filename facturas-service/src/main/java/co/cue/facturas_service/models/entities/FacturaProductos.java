package co.cue.facturas_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facturas_productos")
@DiscriminatorValue("PRODUCTOS")
@Getter
@Setter
@NoArgsConstructor
public class FacturaProductos extends Factura {
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<LineaFactura> lineas = new HashSet<>();
}
