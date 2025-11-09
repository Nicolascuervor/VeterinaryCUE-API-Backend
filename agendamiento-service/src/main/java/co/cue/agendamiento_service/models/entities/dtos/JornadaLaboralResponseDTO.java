package co.cue.agendamiento_service.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class JornadaLaboralResponseDTO {
    private Long id;
    private Long veterinarioId;
    private DayOfWeek diaSemana;
    private LocalTime horaInicioJornada;
    private LocalTime horaFinJornada;
    private LocalTime horaInicioDescanso;
    private LocalTime horaFinDescanso;
    private boolean activa;
}
