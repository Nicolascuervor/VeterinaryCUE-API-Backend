package co.cue.historias_clinicas_service.mapper;

import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.entity.HistorialClinico;
import co.cue.historias_clinicas_service.events.CitaCompletadaEventDTO;
import org.springframework.stereotype.Component;

@Component
public class HistorialClinicoMapper {

    // ---------------------------------------------------------
    // Convierte un evento Kafka de cita completada en una entidad
    // HistorialClinico lista para ser almacenada en la BD.
    // ---------------------------------------------------------

    public HistorialClinico mapEventToEntity(CitaCompletadaEventDTO event) {

        HistorialClinico entity = new HistorialClinico();

        // Se asignan los datos provenientes del evento Kafka
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

        // Los historiales creados desde eventos siempre se marcan como activos
        entity.setActivo(true);

        return entity;
    }

    // ---------------------------------------------------------
    // Convierte un DTO de solicitud (POST/PUT) a la entidad
    // HistorialClinico para guardarlo en la BD.
    // ---------------------------------------------------------

    public HistorialClinico mapRequestToEntity(HistorialClinicoRequestDTO dto) {
        HistorialClinico entity = new HistorialClinico();

        // Asignación de datos enviados desde el cliente (frontend/servicio)
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

        // Por defecto siempre se marca como activo al crear un historial
        entity.setActivo(true);

        return entity;
    }
// ---------------------------------------------------------
    // Convierte una entidad HistorialClinico a un DTO de respuesta
    // (lo que se devuelve al cliente en los endpoints REST).
    // ---------------------------------------------------------

    public HistorialClinicoResponseDTO mapEntityToResponseDTO(HistorialClinico entity) {
        HistorialClinicoResponseDTO dto = new HistorialClinicoResponseDTO();

        // Copia completa de los campos que serán visibles para el cliente
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

        // Timestamps generados automáticamente por Hibernate
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}