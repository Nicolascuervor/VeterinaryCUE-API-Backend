package co.cue.citas_service.dtos;

import co.cue.citas_service.entity.EstadoCita;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class CitaResponseDTO {

    // Id de la cita
    private Long id;

    // Id de la mascota
    private Long petId;

    // Id del veterinario
    private Long veterinarianId;

    // Fecha y hora de la cita
    private LocalDate fechayhora;

    // Motivo de la cita
    private String motivo;

    // Observaciones de la cita
    private String observaciones;

    // Estado general de la mascota
    private String estadoGeneral;

    // Estado de la cita
    private EstadoCita estado;
}
