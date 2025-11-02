package co.cue.inventario_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


@Entity
@Table(name = "productos")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_producto")
@Getter
@Setter
public abstract class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stockActual;

    private Integer stockMinimo;
    private String ubicacion;


    @Column(nullable = false)
    private boolean activo = true;


    private boolean disponibleParaVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)

    @JsonIgnoreProperties("productos")
    private Categoria categoria;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}