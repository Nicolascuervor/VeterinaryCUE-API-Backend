package co.cue.agendamiento_service.models.entities;

import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/** Esta entidad es el "Slot" de tiempo. Es la unidad atómica del calendario de un veterinario. Ej. Vet 1, 10-Nov 9:00, 10-Nov 9:30, DISPONIBLE */
@Entity
@Table(name = "disponibilidad_veterinarios", indexes = {
        @Index(name = "idx_vet_inicio", columnList = "veterinario_id, fecha_hora_inicio")})
@Getter
@Setter
@NoArgsConstructor
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarioId;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;


    @Column(name = "fecha_hora_fin", nullable = false)
    private LocalDateTime fechaHoraFin;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDisponibilidad estado;


    @Column(name = "cita_id", nullable = true, unique = true)
    private Long citaId;

    public Disponibilidad(Long veterinarioId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        this.veterinarioId = veterinarioId;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = EstadoDisponibilidad.DISPONIBLE; // Nace como DISPONIBLE
        this.citaId = null;
    }
}
