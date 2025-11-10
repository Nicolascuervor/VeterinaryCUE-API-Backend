package co.cue.historias_clinicas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "historias_clinicas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mascota_id", nullable = false)
    private Long petId;

    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarianId;

    @Column(name = "cita_id", nullable = true, unique = true)
    private Long citaId;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 1000)
    private String diagnostico;

    @Column(length = 2000)
    private String tratamiento;

    @Column(length = 2000)
    private String observaciones;

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(precision = 4, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Column(name = "estado_general", length = 500)
    private String estadoGeneral;

    @Column(name = "examenes_realizados", length = 1000)
    private String examenesRealizados;

    @Column(name = "medicamentos_recetados", length = 1000)
    private String medicamentosRecetados;

    @Column(name = "proxima_cita")
    private LocalDate proximaCita;

    @Column(nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}