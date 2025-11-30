package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos;

import lombok.Getter;
import lombok.Setter;
@Getter  // Genera automáticamente los métodos get para todos los campos de la clase
@Setter  // Genera automáticamente los métodos set para todos los campos de la clase
public class CirugiaRequestDTO extends  ServicioRequestDTO {   // DTO para solicitudes de creación/actualización de Cirugía, hereda campos comunes de ServicioRequestDTO
    private boolean requiereQuirofano;   // Indica si la cirugía requiere quirófano
    private String notasPreoperatorias;  // Contiene notas preoperatorias relevantes para la cirugía

}