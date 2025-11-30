package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos;
import lombok.Getter;
import lombok.Setter;

@Getter  // Genera automáticamente los métodos get para todos los campos de la clase
@Setter  // Genera automáticamente los métodos set para todos los campos de la clase
public class EsteticaRequestDTO extends ServicioRequestDTO {    // DTO para solicitudes de creación/actualización de Estética, hereda los campos comunes de ServicioRequestDTO

    private String tipoArreglo;  // Especifica el tipo de arreglo o servicio estético solicitado
}
