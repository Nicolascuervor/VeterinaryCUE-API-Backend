package co.cue.historias_clinicas_service.service;

// ... (imports)
import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.events.CitaCompletadaEventDTO;

import java.util.List;

public interface IHistorialClinicoService {

    List<HistorialClinicoResponseDTO> findMedicalRecordsByPetId(Long petId, Long usuarioId);
    HistorialClinicoResponseDTO findMedicalRecordById(Long historialId, Long usuarioId);
    HistorialClinicoResponseDTO createHistorialMedico(HistorialClinicoRequestDTO requestDTO, Long veterinarioId);
    HistorialClinicoResponseDTO updateHistorialMedico(Long historialId, HistorialClinicoRequestDTO requestDTO, Long veterinarioId);
    void deleteHistorialMedico(Long historialId, Long usuarioId);
    void registrarHistorialDesdeEvento(CitaCompletadaEventDTO event);
}