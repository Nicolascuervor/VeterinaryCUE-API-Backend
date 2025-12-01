package co.cue.agendamiento_service.models.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JornadaMasivaRequestDTO {
    private Long veterinarioId;
    // La lista de días a los que se aplicará este horario (Ej: MONDAY, TUESDAY...)
    private List<DayOfWeek> diasSeleccionados;
    private LocalTime horaInicioJornada;
    private LocalTime horaFinJornada;
    private LocalTime horaInicioDescanso;
    private LocalTime horaFinDescanso;
}