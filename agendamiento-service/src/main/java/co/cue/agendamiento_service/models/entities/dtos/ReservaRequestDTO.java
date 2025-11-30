package co.cue.agendamiento_service.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter   // Genera automáticamente los métodos get para todos los campos
@Setter   // Genera automáticamente los métodos set para todos los campos
public class ReservaRequestDTO {             // DTO para realizar una reserva de cita o disponibilidad
    private Long citaId;                     // ID de la cita que se desea reservar
    private List<Long> idsDisponibilidad;    // Lista de IDs de slots de disponibilidad que se quieren reservar
}
