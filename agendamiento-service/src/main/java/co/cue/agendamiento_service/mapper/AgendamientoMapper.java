package co.cue.agendamiento_service.mapper;

import co.cue.agendamiento_service.models.entities.Disponibilidad;
import co.cue.agendamiento_service.models.entities.JornadaLaboral;
import co.cue.agendamiento_service.models.entities.dtos.DisponibilidadResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;
import org.springframework.stereotype.Component;

@Component  // Marca esta clase como un componente Spring para que pueda ser inyectada en otros beans.
public class AgendamientoMapper {

    //  MÉTODO PARA MAPEAR DISPONIBILIDAD
    public DisponibilidadResponseDTO toDisponibilidadResponseDTO(Disponibilidad entity) {
        if (entity == null) { // Verifica si la entidad es null, en cuyo caso devuelve null para evitar NullPointerException.
            return null;
        }

        DisponibilidadResponseDTO dto = new DisponibilidadResponseDTO(); // Crea un nuevo DTO de respuesta.
        dto.setId(entity.getId());                                       // Copia el ID de la entidad al DTO.
        dto.setVeterinarioId(entity.getVeterinarioId());                 // Copia el ID del veterinario asociado.
        dto.setFechaHoraInicio(entity.getFechaHoraInicio());             // Copia la fecha y hora de inicio del slot.
        dto.setFechaHoraFin(entity.getFechaHoraFin());                   // Copia la fecha y hora de fin del slot.
        dto.setEstado(entity.getEstado());                               // Copia el estado de disponibilidad del slot.
        dto.setCitaId(entity.getCitaId());                               // Copia el ID de la cita asociada, si existe.

        return dto;   // Retorna el DTO mapeado.
    }


    // MÉTODO PARA MAPEAR JORNADA LABORAL
    public JornadaLaboralResponseDTO toJornadaResponseDTO(JornadaLaboral entity) {
        if (entity == null) { // Verifica si la entidad es null, en cuyo caso devuelve null.
            return null;
        }

        JornadaLaboralResponseDTO dto = new JornadaLaboralResponseDTO();   // Crea un nuevo DTO de respuesta para jornada laboral.
        dto.setId(entity.getId());                                         // Copia el ID de la jornada laboral.
        dto.setVeterinarioId(entity.getVeterinarioId());                   // Copia el ID del veterinario asociado a la jornada.
        dto.setDiaSemana(entity.getDiaSemana());                           // Copia el día de la semana correspondiente a la jornada.
        dto.setHoraInicioJornada(entity.getHoraInicioJornada());           // Copia la hora de inicio de la jornada laboral.
        dto.setHoraFinJornada(entity.getHoraFinJornada());                 // Copia la hora de fin de la jornada laboral.
        dto.setHoraInicioDescanso(entity.getHoraInicioDescanso());         // Copia la hora de inicio del descanso.
        dto.setHoraFinDescanso(entity.getHoraFinDescanso());               // Copia la hora de fin del descanso.
        dto.setActiva(entity.isActiva());                                  // Copia si la jornada está activa o no.

        return dto; // Retorna el DTO mapeado.
    }
}
