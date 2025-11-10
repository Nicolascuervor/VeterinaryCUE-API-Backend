package co.cue.citas_service.service;

import co.cue.citas_service.dtos.CitaRequestDTO;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.entity.Cita;

import java.util.List;

public interface ICitaService {
    List<Cita> findCitaByEstado(String estado);
    CitaResponseDTO findCitaById (Long id);
    CitaResponseDTO createCita(CitaRequestDTO citaDTO, Long usuarioId);
    CitaUpdateDTO updateCita(Long id,CitaUpdateDTO citaUpdateDTO);
    void deleteCita(Long id);

}
