package co.cue.agendamiento_service.mapper;

import co.cue.agendamiento_service.models.entities.JornadaLaboral;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;
import org.springframework.stereotype.Component;
import co.cue.agendamiento_service.models.entities.OcupacionAgenda;
import co.cue.agendamiento_service.models.entities.dtos.OcupacionResponseDTO;

@Component
public class AgendamientoMapper {

    // --- NUEVO MÉTODO ---
    // Convierte la entidad de BD a la cajita de color para el Front
    public OcupacionResponseDTO toOcupacionResponseDTO(OcupacionAgenda entity) {
        if (entity == null) return null;

        return OcupacionResponseDTO.builder()
                .id(entity.getId())
                .veterinarioId(entity.getVeterinarioId())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .tipo(entity.getTipo())
                .referenciaExternaId(entity.getReferenciaExternaId()) // Importante para enlazar con la Cita
                .observacion(entity.getObservacion())
                .build();
    }

    // --- SE MANTIENE IGUAL ---
    public JornadaLaboralResponseDTO toJornadaResponseDTO(JornadaLaboral entity) {
        if (entity == null) return null;

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

    // NOTA: Puedes borrar el método antiguo 'toDisponibilidadResponseDTO' ya que esa clase ya no existe.
}
