package co.cue.historias_clinicas_service.mapper;

import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.entity.HistorialClinico;
import co.cue.historias_clinicas_service.events.CitaCompletadaEventDTO;
import org.springframework.stereotype.Component;

@Component
public class HistorialClinicoMapper {

    public HistorialClinico mapEventToEntity(CitaCompletadaEventDTO event) {
        HistorialClinico entity = new HistorialClinico();
        entity.setCitaId(event.getCitaId());
        entity.setPetId(event.getPetId());
        entity.setVeterinarianId(event.getVeterinarianId());
        entity.setFecha(event.getFecha());
        entity.setDiagnostico(event.getDiagnostico());
        entity.setTratamiento(event.getTratamiento());
        entity.setObservaciones(event.getObservaciones());
        entity.setPeso(event.getPeso());
        entity.setTemperatura(event.getTemperatura());
        entity.setFrecuenciaCardiaca(event.getFrecuenciaCardiaca());
        entity.setFrecuenciaRespiratoria(event.getFrecuenciaRespiratoria());
        entity.setEstadoGeneral(event.getEstadoGeneral());
        entity.setExamenesRealizados(event.getExamenesRealizados());
        entity.setMedicamentosRecetados(event.getMedicamentosRecetados());
        entity.setProximaCita(event.getProximaCita());
        entity.setActivo(true); // Nace activo

        return entity;
    }

    public HistorialClinico mapRequestToEntity(HistorialClinicoRequestDTO dto) {
        HistorialClinico entity = new HistorialClinico();
        entity.setPetId(dto.getPetId());
        entity.setVeterinarianId(dto.getVeterinarianId());
        entity.setFecha(dto.getFecha());
        entity.setDiagnostico(dto.getDiagnostico());
        entity.setTratamiento(dto.getTratamiento());
        entity.setObservaciones(dto.getObservaciones());
        entity.setPeso(dto.getPeso());
        entity.setTemperatura(dto.getTemperatura());
        entity.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        entity.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        entity.setEstadoGeneral(dto.getEstadoGeneral());
        entity.setExamenesRealizados(dto.getExamenesRealizados());
        entity.setMedicamentosRecetados(dto.getMedicamentosRecetados());
        entity.setProximaCita(dto.getProximaCita());
        entity.setActivo(true);

        return entity;
    }


    public HistorialClinicoResponseDTO mapEntityToResponseDTO(HistorialClinico entity) {
        HistorialClinicoResponseDTO dto = new HistorialClinicoResponseDTO();
        dto.setId(entity.getId());
        dto.setPetId(entity.getPetId());
        dto.setVeterinarianId(entity.getVeterinarianId());
        dto.setFecha(entity.getFecha());
        dto.setDiagnostico(entity.getDiagnostico());
        dto.setTratamiento(entity.getTratamiento());
        dto.setObservaciones(entity.getObservaciones());
        dto.setPeso(entity.getPeso());
        dto.setTemperatura(entity.getTemperatura());
        dto.setFrecuenciaCardiaca(entity.getFrecuenciaCardiaca());
        dto.setFrecuenciaRespiratoria(entity.getFrecuenciaRespiratoria());
        dto.setEstadoGeneral(entity.getEstadoGeneral());
        dto.setExamenesRealizados(entity.getExamenesRealizados());
        dto.setMedicamentosRecetados(entity.getMedicamentosRecetados());
        dto.setProximaCita(entity.getProximaCita());
        dto.setActivo(entity.getActivo());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}