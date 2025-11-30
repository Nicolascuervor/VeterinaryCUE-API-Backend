package co.cue.carrito_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
// Entidad que representa un carrito de compras
public class Carrito {

    // ID del carrito
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del usuario propietario (opcional)
    @Column(name = "usuario_id", unique = true, nullable = true)
    private Long usuarioId;

    // ID de la sesión si es un carrito anónimo
    @Column(name = "session_id", unique = true, nullable = true)
    private String sessionId;

    // Conjunto de items asociados al carrito
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ItemCarrito> items = new HashSet<>();

    // Fecha de creación del carrito
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt;

    // Fecha de última actualización del carrito
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
