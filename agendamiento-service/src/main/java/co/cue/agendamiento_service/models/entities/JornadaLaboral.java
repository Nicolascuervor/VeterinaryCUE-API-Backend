package co.cue.agendamiento_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

/** Esta entidad define la plantilla de trabajo semanal para un veterinario. */
@Entity
@Table(name = "jornadas_laborales", uniqueConstraints = {
        // Un veterinario solo puede tener una plantilla por día.
        @UniqueConstraint(columnNames = {"veterinario_id", "dia_semana"})
})
@Getter
@Setter
@NoArgsConstructor
public class JornadaLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarioId;


    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;


    @Column(name = "hora_inicio_jornada", nullable = false)
    private LocalTime horaInicioJornada;

    @Column(name = "hora_fin_jornada", nullable = false)
    private LocalTime horaFinJornada;


    @Column(name = "hora_inicio_descanso", nullable = true)
    private LocalTime horaInicioDescanso;

    @Column(name = "hora_fin_descanso", nullable = true)
    private LocalTime horaFinDescanso;


    @Column(nullable = false)
    private boolean activa = true;

    public JornadaLaboral(Long veterinarioId, DayOfWeek diaSemana, LocalTime horaInicioJornada, LocalTime horaFinJornada, LocalTime horaInicioDescanso, LocalTime horaFinDescanso) {
        this.veterinarioId = veterinarioId;
        this.diaSemana = diaSemana;
        this.horaInicioJornada = horaInicioJornada;
        this.horaFinJornada = horaFinJornada;
        this.horaInicioDescanso = horaInicioDescanso;
        this.horaFinDescanso = horaFinDescanso;
        this.activa = true;
    }
}
