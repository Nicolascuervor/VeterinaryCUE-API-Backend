package co.cue.citas_service.dtos;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CitaRequestDTO {
    private Long petId;
    private Long veterinarianId;
    private Long servicioId;
    private List<Long> idsDisponibilidad;
    private String motivoConsulta;
    private String estadoGeneralMascota;
}