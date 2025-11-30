package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

@Getter   // Genera automáticamente los métodos get para todos los campos
@Setter    // Genera automáticamente los métodos set para todos los campos
public class VacunacionResponseDTO extends ServicioResponseDTO {    // DTO de respuesta específico para servicios de vacunación
    private String nombreBiologico;    // Nombre del biológico o vacuna asociada al servicio
}
