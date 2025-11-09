package co.cue.agendamiento_service.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ReservaRequestDTO {
    private Long citaId;
    private List<Long> idsDisponibilidad;
}
