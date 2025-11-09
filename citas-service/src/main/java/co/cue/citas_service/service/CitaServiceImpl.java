package co.cue.citas_service.service;

import co.cue.citas_service.dto.CitaRequestDTO;
import co.cue.citas_service.dto.CitaResponseDTO;
import co.cue.citas_service.dto.CitaUpdateDTO;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.repository.CitaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class CitaServiceImpl implements ICitaService{
    private final CitaRepository citaRepository;
    @Override
    public List<Cita> findCitaByEstado(String estado) {
        return List.of();
    }

    @Override
    public CitaResponseDTO findCitaById(Long id) {
        return (CitaResponseDTO) List.of();
    }

    @Override
    public CitaResponseDTO createCita(CitaRequestDTO citaDTO) {
        return null;
    }

    @Override
    public CitaUpdateDTO updateCita(Long id, CitaUpdateDTO citaUpdateDTO) {
        return null;
    }

    @Override
    public void deleteCita(Long id) {

    }
}
