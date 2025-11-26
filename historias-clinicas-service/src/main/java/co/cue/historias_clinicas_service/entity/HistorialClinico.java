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

    // Identificador único del historial clínico
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de la mascota asociada al historial
    @Column(name = "mascota_id", nullable = false)
    private Long petId;

    // ID del veterinario responsable del registro
    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarianId;

    // ID de la cita asociada (opcional, único si existe)
    @Column(name = "cita_id", nullable = true, unique = true)
    private Long citaId;

    // Fecha en la que fue atendida la mascota
    @Column(nullable = false)
    private LocalDate fecha;

    // Diagnóstico principal realizado al paciente
    @Column(nullable = false, length = 1000)
    private String diagnostico;

    // Tratamiento indicado por el veterinario
    @Column(length = 2000)
    private String tratamiento;

    // Observaciones adicionales del estado de la mascota
    @Column(length = 2000)
    private String observaciones;

    // Peso de la mascota en la cita (máx 999.99)
    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    // Temperatura corporal de la mascota (máx 99.99)
    @Column(precision = 4, scale = 2)
    private BigDecimal temperatura;

    // Frecuencia cardíaca registrada
    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria registrada
    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    // Estado general del paciente (ánimo, hidratación, etc.)
    @Column(name = "estado_general", length = 500)
    private String estadoGeneral;

    // Exámenes realizados durante la consulta
    @Column(name = "examenes_realizados", length = 1000)
    private String examenesRealizados;

    // Medicamentos recetados en la cita
    @Column(name = "medicamentos_recetados", length = 1000)
    private String medicamentosRecetados;

    // Fecha para la próxima cita
    @Column(name = "proxima_cita")
    private LocalDate proximaCita;

    // Indica si el historial está activo (soft delete)
    @Column(nullable = false)
    private Boolean activo = true;

    // Fecha y hora en que se creó el historial
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Fecha y hora de última actualización
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}