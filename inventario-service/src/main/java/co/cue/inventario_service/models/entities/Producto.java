package co.cue.inventario_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "productos")
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia para herencia: cada subclase va a su propia tabla
@DiscriminatorColumn(name = "tipo_producto")  // Define el tipo de producto (ALIMENTO, MEDICINA, ACCESORIO, KIT)
@Getter
@Setter
public abstract class Producto {

    // Identificador único del producto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del producto (no puede repetirse)
    @Column(nullable = false, unique = true)
    private String nombre;

    // Descripción del producto para mostrar en el ecommerce
    @Column(length = 1000)
    private String descripcion;

    // URL o ruta de la foto/imagen del producto
    @Column(length = 500)
    private String foto;

    // Precio del producto
    @Column(nullable = false)
    private Double precio;

    // Cantidad disponible en inventario
    @Column(nullable = false)
    private Integer stockActual;

    // Cantidad mínima deseada antes de alertar
    private Integer stockMinimo;

    // Ubicación física dentro del inventario
    private String ubicacion;


    // Indica si el producto está activo (soft delete)
    @Column(nullable = false)
    private boolean activo = true;


    // Indica si el producto puede ser vendido
    private boolean disponibleParaVenta;



    // Indica si el producto es un kit (conjunto de productos)
    @Column(name = "es_kit")
    private boolean esKit = false;


    // Relación para manejar los productos que componen un kit
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "producto_componentes",
            joinColumns = @JoinColumn(name = "kit_id"),
            inverseJoinColumns = @JoinColumn(name = "componente_id")
    )
    private Set<Producto> componentes = new HashSet<>();


    // Categoría a la que pertenece el producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonIgnoreProperties("productos") // Evita recursividad en respuestas JSON
    private Categoria categoria;


    // Fecha de creación del registro
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Fecha de última actualización
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}