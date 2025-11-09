package co.cue.historias_clinicas_service.service;

import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.entity.HistorialClinico;

import java.util.List;

public interface IHistorialClinicoService {

    List<HistorialClinico> findByPetId(Long petId);
    HistorialClinicoResponseDTO findMedicalRecordByPetId(Long petId);
    HistorialClinicoResponseDTO createHistorialMedico(HistorialClinicoRequestDTO historialClinicoRequestDTO);
    HistorialClinicoResponseDTO updateHistorialMedico(Long id, HistorialClinicoRequestDTO historialClinicoRequestDTO);
    void deleteHistorialMedico(Long id);
}
