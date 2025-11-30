package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos;

import lombok.Getter;
import lombok.Setter;


@Getter   // Genera automáticamente los métodos get para todos los campos de la clase
@Setter   // Genera automáticamente los métodos set para todos los campos de la clase
public class CirugiaResponseDTO extends ServicioResponseDTO {    // DTO de respuesta para Cirugía, extiende los campos comunes de ServicioResponseDTO
    private boolean requiereQuirofano;      // Indica si la cirugía requiere quirófano
    private String notasPreoperatorias;     // Notas preoperatorias específicas de la cirugía
}
