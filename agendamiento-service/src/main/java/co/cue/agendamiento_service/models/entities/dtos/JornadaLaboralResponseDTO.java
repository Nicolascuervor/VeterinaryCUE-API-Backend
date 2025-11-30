package co.cue.agendamiento_service.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter  // Genera automáticamente los métodos get para todos los campos
@Setter  // Genera automáticamente los métodos set para todos los campos
public class JornadaLaboralResponseDTO {    // DTO de respuesta que representa la jornada laboral de un veterinario
    private Long id;                         // ID de la jornada laboral
    private Long veterinarioId;              // ID del veterinario al que pertenece la jornada
    private DayOfWeek diaSemana;             // Día de la semana de la jornada
    private LocalTime horaInicioJornada;    // Hora de inicio de la jornada laboral
    private LocalTime horaFinJornada;       // Hora de fin de la jornada laboral
    private LocalTime horaInicioDescanso;   // Hora de inicio del periodo de descanso dentro de la jornada
    private LocalTime horaFinDescanso;      // Hora de fin del periodo de descanso dentro de la jornada
    private boolean activa;                // Indica si la jornada está activa o desactivada
}
