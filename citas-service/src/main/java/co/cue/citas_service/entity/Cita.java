package co.cue.citas_service.entity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
public class Cita {

    // Id de la cita
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Id de la mascota
    @Column(name = "mascota_id", nullable = false)
    private Long petId;

    // Id del dueño de la mascota
    @Column(name = "duenio_id", nullable = false)
    private Long duenioId;

    // Id del veterinario
    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarianId;


    // Id del servicio asociado
    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    // Nombre del servicio
    @Column(name = "nombre_servicio", nullable = false)
    private String nombreServicio;

    // Precio del servicio
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioServicio;

    // Fecha y hora de inicio de la cita
    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;

    // Fecha y hora de fin de la cita
    @Column(nullable = false)
    private LocalDateTime fechaHoraFin;

    // Estado de la cita
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado;

    // Tratamiento realizado
    @Column(length = 2000)
    private String tratamiento;

    // Observaciones de la cita
    @Column(length = 2000)
    private String observaciones;

    // Estado general de la mascota
    @Column(length = 2000)
    private String estadoGeneralMascota;

    // Diagnóstico de la mascota
    @Column(length = 2000)
    private String diagnostico;

    // Motivo de la consulta
    @Column(length = 2000)
    private String motivoConsulta;



    // Peso de la mascota
    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    // Temperatura de la mascota
    @Column(precision = 4, scale = 2)
    private BigDecimal temperatura;

    // Frecuencia cardiaca
    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria
    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    // Exámenes realizados
    @Column(name = "examenes_realizados", length = 1000)
    private String examenesRealizados;

    // Medicamentos recetados
    @Column(name = "medicamentos_recetados", length = 1000)
    private String medicamentosRecetados;

    // Fecha de la próxima cita
    @Column(name = "proxima_cita")
    private LocalDate proximaCita;

    // Token único para confirmar la cita mediante link en el correo
    @Column(name = "token_confirmacion", unique = true, length = 100)
    private String tokenConfirmacion;

    // Fecha de creación del registro
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    // Fecha de última actualización del registro
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}