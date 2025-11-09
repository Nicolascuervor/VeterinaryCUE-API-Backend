package co.cue.agendamiento_service.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Getter
@Setter
public class ReservaRequestDTO {
    @NotNull
    private Long citaId;
    private List<Long> idsDisponibilidad;
}
