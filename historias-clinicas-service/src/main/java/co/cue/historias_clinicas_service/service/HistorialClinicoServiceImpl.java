package co.cue.historias_clinicas_service.service;

import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.entity.HistorialClinico;
import co.cue.historias_clinicas_service.repository.HistorialClinicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistorialClinicoServiceImpl implements IHistorialClinicoService {

    private final HistorialClinicoRepository historialClinicoRepository;


    @Override
    public List<HistorialClinico> findByPetId(Long petId) {
        return List.of();
    }

    @Override
    public HistorialClinicoResponseDTO findMedicalRecordByPetId(Long petId) {
        return null;
    }

    @Override
    public HistorialClinicoResponseDTO createHistorialMedico(HistorialClinicoRequestDTO historialClinicoRequestDTO) {
        return null;
    }

    @Override
    public HistorialClinicoResponseDTO updateHistorialMedico(Long id, HistorialClinicoRequestDTO historialClinicoRequestDTO) {
        return null;
    }

    @Override
    public void deleteHistorialMedico(Long id) {

    }
}
