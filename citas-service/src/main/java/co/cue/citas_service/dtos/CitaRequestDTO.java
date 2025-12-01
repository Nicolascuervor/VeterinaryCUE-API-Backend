package co.cue.citas_service.dtos;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CitaRequestDTO {

    // ID de la mascota para la cual se agenda la cita
    private Long petId;

    // ID del veterinario encargado de atender la cita
    private Long veterinarianId;

    // ID del servicio que se desea agendar (baño, consulta, cirugía, etc.)
    // Esto determinará la duración y el precio.
    private Long servicioId;


    private LocalDateTime fechaInicio;

    // Motivo principal por el cual se agenda la cita
    private String motivoConsulta;

    // Estado general de la mascota al momento de solicitar la cita
    private String estadoGeneralMascota;
}