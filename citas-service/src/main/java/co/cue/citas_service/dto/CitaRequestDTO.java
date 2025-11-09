package co.cue.citas_service.dto;
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
    private String motivoConsulta; // El motivo del dueño
    private String estadoGeneralMascota; // Cómo llega la mascota (opcional al crear)
}