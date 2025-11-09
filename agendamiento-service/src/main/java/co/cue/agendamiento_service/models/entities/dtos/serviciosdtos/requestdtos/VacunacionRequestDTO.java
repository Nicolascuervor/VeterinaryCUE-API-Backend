package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VacunacionRequestDTO extends ServicioRequestDTO{
    private String nombreBiologico;
}
