package co.cue.citas_service.mapper;
import co.cue.citas_service.dtos.CitaDetailDTO;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.events.CitaCompletadaEventDTO;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    // Actualiza los campos de la entidad Cita con la información del DTO
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
        
        // Actualizar fechas si se proporcionan (para reasignación de horario)
        if (dto.getFechaHoraInicio() != null) {
            cita.setFechaHoraInicio(dto.getFechaHoraInicio());
        }
        if (dto.getFechaHoraFin() != null) {
            cita.setFechaHoraFin(dto.getFechaHoraFin());
        }
    }

    // Convierte una entidad Cita a un DTO de respuesta para el cliente
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
        dto.setFechaHoraInicio(cita.getFechaHoraInicio());

        return dto;
    }

    // Convierte una entidad Cita a un DTO para evento de cita completada
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

    public CitaDetailDTO mapToDetailDTO(Cita cita) {
        CitaDetailDTO dto = new CitaDetailDTO();

        // Datos Base
        dto.setId(cita.getId());
        dto.setDuenioId(cita.getDuenioId());
        dto.setPetId(cita.getPetId());
        dto.setVeterinarianId(cita.getVeterinarianId());
        dto.setServicioId(cita.getServicioId());
        dto.setFechaHoraInicio(cita.getFechaHoraInicio()); // ¡Con hora!
        dto.setEstado(cita.getEstado());
        dto.setNombreServicio(cita.getNombreServicio());
        dto.setPrecioServicio(cita.getPrecioServicio());

        // Datos Clínicos (Pueden ser null al inicio, no pasa nada)
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setEstadoGeneralMascota(cita.getEstadoGeneralMascota());
        // Convertimos BigDecimal a Double si es necesario, o mantenemos BigDecimal en el DTO
        // Asumiendo que en CitaDetailDTO usaste Double para peso/temp:
        dto.setPeso(cita.getPeso() != null ? cita.getPeso().doubleValue() : null);
        dto.setTemperatura(cita.getTemperatura() != null ? cita.getTemperatura().doubleValue() : null);

        dto.setFrecuenciaCardiaca(cita.getFrecuenciaCardiaca());
        dto.setFrecuenciaRespiratoria(cita.getFrecuenciaRespiratoria());
        dto.setDiagnostico(cita.getDiagnostico());
        dto.setTratamiento(cita.getTratamiento());
        dto.setMedicamentosRecetados(cita.getMedicamentosRecetados());
        dto.setObservaciones(cita.getObservaciones());
        dto.setExamenesRealizados(cita.getExamenesRealizados());

        return dto;
    }




}