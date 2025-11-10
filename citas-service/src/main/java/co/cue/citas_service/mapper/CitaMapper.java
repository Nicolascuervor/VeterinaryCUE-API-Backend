package co.cue.citas_service.mapper;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.events.CitaCompletadaEventDTO;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public void updateEntityFromDTO(CitaUpdateDTO dto, Cita cita) {
        cita.setEstadoGeneralMascota(dto.getEstadoGeneralMascota());
        cita.setObservaciones(dto.getObservaciones());
        cita.setDiagnostico(dto.getDiagnostico());
        cita.setTratamiento(dto.getTratamiento());
        cita.setPeso(dto.getPeso());
        cita.setTemperatura(dto.getTemperatura());
        cita.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        cita.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        cita.setExamenesRealizados(dto.getExamenesRealizados());
        cita.setMedicamentosRecetados(dto.getMedicamentosRecetados());
        cita.setProximaCita(dto.getProximaCita());
    }

    public CitaResponseDTO mapToResponseDTO(Cita cita) {
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId(cita.getId());
        dto.setPetId(cita.getPetId());
        dto.setVeterinarianId(cita.getVeterinarianId());
        dto.setFechayhora(cita.getFechaHoraInicio().toLocalDate());
        dto.setMotivo(cita.getMotivoConsulta());
        dto.setObservaciones(cita.getObservaciones());
        dto.setEstadoGeneral(cita.getEstadoGeneralMascota());
        dto.setEstado(cita.getEstado());
        return dto;
    }

    public CitaCompletadaEventDTO mapToCitaCompletadaEvent(Cita cita) {
        return CitaCompletadaEventDTO.builder()
                .citaId(cita.getId())
                .petId(cita.getPetId())
                .veterinarianId(cita.getVeterinarianId())
                .fecha(cita.getFechaHoraInicio().toLocalDate())
                .diagnostico(cita.getDiagnostico())
                .tratamiento(cita.getTratamiento())
                .observaciones(cita.getObservaciones())
                .peso(cita.getPeso())
                .temperatura(cita.getTemperatura())
                .frecuenciaCardiaca(cita.getFrecuenciaCardiaca())
                .frecuenciaRespiratoria(cita.getFrecuenciaRespiratoria())
                .estadoGeneral(cita.getEstadoGeneralMascota())
                .examenesRealizados(cita.getExamenesRealizados())
                .medicamentosRecetados(cita.getMedicamentosRecetados())
                .proximaCita(cita.getProximaCita())
                .build();
    }
}