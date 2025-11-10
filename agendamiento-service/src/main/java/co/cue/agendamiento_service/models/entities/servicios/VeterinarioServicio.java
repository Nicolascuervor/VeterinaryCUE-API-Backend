package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // (Mentor): ¡Faltaba esto!
@Table(name = "veterinario_servicios")
@Getter // (Colega Senior): ¡Esta es la corrección clave!
@Setter // (Mentor): Buena práctica añadirlo también
@NoArgsConstructor // (Mentor): Requerido por JPA
public class VeterinarioServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long veterinarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;
}