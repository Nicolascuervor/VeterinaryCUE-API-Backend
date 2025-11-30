package co.cue.inventario_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identificador único autogenerado
    private Long id;


    @Column(nullable = false, unique = true, length = 100)
    // Nombre de la categoría (obligatorio y único)
    private String nombre;

    @Column(length = 500)
    // Descripción opcional de la categoría
    private String descripcion;


    @Column(nullable = false)
    // Indica si la categoría está activa (soft delete)
    private boolean activo = true;

    @OneToMany(mappedBy = "categoria", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)

    // Relación con los productos que pertenecen a la categoría
    private Set<Producto> productos = new HashSet<>();
}
