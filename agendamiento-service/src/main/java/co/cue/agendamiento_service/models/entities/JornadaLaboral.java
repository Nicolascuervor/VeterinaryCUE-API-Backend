package co.cue.agendamiento_service.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Esta entidad define la plantilla de trabajo semanal para un veterinario.
 * Cada registro representa un día específico con horarios de jornada y descanso.
 */
@Entity  // Marca la clase como entidad JPA para persistencia en la base de datos
// Define la tabla específica para jornadas laborales
@Table(name = "jornadas_laborales", uniqueConstraints = {
        // Un veterinario solo puede tener una plantilla por día.
        @UniqueConstraint(columnNames = {"veterinario_id", "dia_semana"})
})
@Getter // Genera automáticamente los getters para todos los campos
@Setter  // Genera automáticamente los setters para todos los campos
@NoArgsConstructor  // Genera un constructor sin parámetros
public class JornadaLaboral {

    @Id  // Define la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // La clave primaria se genera automáticamente por la base de datos
    private Long id;

    @Column(name = "veterinario_id", nullable = false)
    // Columna obligatoria que almacena el ID del veterinario
    private Long veterinarioId;


    @Enumerated(EnumType.STRING)
    // Indica que el enum DayOfWeek se almacenará como String en la base de datos
    @Column(name = "dia_semana", nullable = false)
    // Columna obligatoria que indica el día de la semana de la jornada
    private DayOfWeek diaSemana;


    @Column(name = "hora_inicio_jornada", nullable = false)
    // Columna obligatoria que indica la hora de inicio de la jornada laboral
    private LocalTime horaInicioJornada;

    @Column(name = "hora_fin_jornada", nullable = false)
    // Columna obligatoria que indica la hora de fin de la jornada laboral
    private LocalTime horaFinJornada;


    @Column(name = "hora_inicio_descanso", nullable = true)
    // Columna opcional que indica la hora de inicio del descanso
    private LocalTime horaInicioDescanso;

    @Column(name = "hora_fin_descanso", nullable = true)
    // Columna opcional que indica la hora de fin del descanso
    private LocalTime horaFinDescanso;


    @Column(nullable = false)
    // Columna obligatoria que indica si la jornada está activa o no
    private boolean activa = true;

}
