package co.cue.citas_service.entity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mascota_id", nullable = false)
    private Long petId;

    @Column(name = "duenio_id", nullable = false)
    private Long duenioId;

    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarianId;


    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    @Column(name = "nombre_servicio", nullable = false)
    private String nombreServicio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioServicio;

    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(nullable = false)
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado;


    @Column(length = 1000)
    private String motivoConsulta; // Razón del dueño para la visita

    @Column(length = 500)
    private String estadoGeneralMascota; // Cómo llegó la mascota (ej. "Apatica, sin apetito")

    @Column(length = 2000)
    private String observaciones; // Notas libres del veterinario

    @Column(length = 1000)
    private String diagnostico; // El diagnóstico final del vet

    @Column(length = 2000)
    private String tratamiento; // El tratamiento recetado

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(precision = 4, scale = 2)
    private BigDecimal temperatura;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}