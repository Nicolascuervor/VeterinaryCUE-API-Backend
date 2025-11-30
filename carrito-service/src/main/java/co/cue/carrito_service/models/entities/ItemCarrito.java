package co.cue.carrito_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items_carrito")
@Getter
@Setter
@NoArgsConstructor
// Entidad que representa un item dentro de un carrito de compras
public class ItemCarrito {

    // ID del item
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del producto asociado
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    // Cantidad de este producto en el carrito
    @Column(nullable = false)
    private Integer cantidad;


    // Carrito al que pertenece este item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

}
