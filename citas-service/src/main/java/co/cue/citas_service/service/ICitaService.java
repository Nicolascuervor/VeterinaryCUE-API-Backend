package co.cue.citas_service.service;

import co.cue.citas_service.dto.CitaRequestDTO;
import co.cue.citas_service.dto.CitaResponseDTO;
import co.cue.citas_service.dto.CitaUpdateDTO;
import co.cue.citas_service.entity.Cita;

import java.util.List;

public interface ICitaService {
    List<Cita> findCitaByEstado(String estado);
    CitaResponseDTO findCitaById (Long id);
    CitaResponseDTO createCita(CitaRequestDTO citaDTO);
    CitaUpdateDTO updateCita(Long id,CitaUpdateDTO citaUpdateDTO);
    void deleteCita(Long id);

}
