package co.cue.agendamiento_service.mapper;

import co.cue.agendamiento_service.models.entities.Disponibilidad;
import co.cue.agendamiento_service.models.entities.JornadaLaboral;
import co.cue.agendamiento_service.models.entities.dtos.DisponibilidadResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AgendamientoMapper {


    public DisponibilidadResponseDTO toDisponibilidadResponseDTO(Disponibilidad entity) {
        if (entity == null) {
            return null;
        }

        DisponibilidadResponseDTO dto = new DisponibilidadResponseDTO();
        dto.setId(entity.getId());
        dto.setVeterinarioId(entity.getVeterinarioId());
        dto.setFechaHoraInicio(entity.getFechaHoraInicio());
        dto.setFechaHoraFin(entity.getFechaHoraFin());
        dto.setEstado(entity.getEstado());
        dto.setCitaId(entity.getCitaId());

        return dto;
    }
    public JornadaLaboralResponseDTO toJornadaResponseDTO(JornadaLaboral entity) {
        if (entity == null) {
            return null;
        }

        JornadaLaboralResponseDTO dto = new JornadaLaboralResponseDTO();
        dto.setId(entity.getId());
        dto.setVeterinarioId(entity.getVeterinarioId());
        dto.setDiaSemana(entity.getDiaSemana());
        dto.setHoraInicioJornada(entity.getHoraInicioJornada());
        dto.setHoraFinJornada(entity.getHoraFinJornada());
        dto.setHoraInicioDescanso(entity.getHoraInicioDescanso());
        dto.setHoraFinDescanso(entity.getHoraFinDescanso());
        dto.setActiva(entity.isActiva());

        return dto;
    }
}
