package co.cue.citas_service.dtos;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CitaRequestDTO {

    // ID de la mascota para la cual se agenda la cita
    private Long petId;

    // ID del veterinario encargado de atender la cita
    private Long veterinarianId;

    // ID del servicio que se desea agendar (baño, consulta, cirugía, etc.)
    private Long servicioId;

    // Lista de IDs de disponibilidad (slots horarios) seleccionados para la cita
    private List<Long> idsDisponibilidad;

    // Motivo principal por el cual se agenda la cita
    private String motivoConsulta;

    // Estado general de la mascota al momento de solicitar la cita
    private String estadoGeneralMascota;
}