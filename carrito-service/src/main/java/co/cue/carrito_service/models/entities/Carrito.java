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
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Clave para usuarios registrados. Puede ser nulo si es invitado.
    @Column(name = "usuario_id", unique = true, nullable = true)
    private Long usuarioId;

    // Clave para usuarios invitados. Puede ser nulo si está registrado.
    @Column(name = "session_id", unique = true, nullable = true)
    private String sessionId;

    // Relación de composición. Si se borra el carrito, se borran sus items.
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ItemCarrito> items = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
