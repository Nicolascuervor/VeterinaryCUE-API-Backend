package co.cue.agendamiento_service.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class JornadaLaboralRequestDTO {
    @NotNull
    private Long veterinarioId;
    @NotNull
    private DayOfWeek diaSemana;
    @NotNull
    private LocalTime horaInicioJornada;
    @NotNull
    private LocalTime horaFinJornada;
    private LocalTime horaInicioDescanso;
    private LocalTime horaFinDescanso;
}
