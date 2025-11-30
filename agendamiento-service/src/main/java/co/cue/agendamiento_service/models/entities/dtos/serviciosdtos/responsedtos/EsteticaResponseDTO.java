package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos;
import lombok.Getter;
import lombok.Setter;

@Getter    // Genera automáticamente los métodos get para todos los campos de la clase
@Setter     // Genera automáticamente los métodos set para todos los campos de la clase
public class EsteticaResponseDTO extends ServicioResponseDTO {    // DTO de respuesta para Estética, extiende los campos comunes de ServicioResponseDTO
    private String tipoArreglo;   // Tipo de arreglo o tratamiento estético que se realizará
}
