package co.cue.citas_service.dtos;

import co.cue.citas_service.entity.EstadoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta de confirmación de cita.
 * Incluye toda la información necesaria para mostrar en la página de confirmación.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaConfirmacionResponseDTO {
    
    private Long id;
    private EstadoCita estado;
    private String mensaje;
    
    // Información de la mascota
    private String nombreMascota;
    private Long petId;
    
    // Información del veterinario
    private String nombreVeterinario;
    private Long veterinarianId;
    
    // Información de la cita
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String nombreServicio;
    private String motivoConsulta;
}

