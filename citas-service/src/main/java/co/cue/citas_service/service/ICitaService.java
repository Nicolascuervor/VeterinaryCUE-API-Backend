package co.cue.citas_service.service;

import co.cue.citas_service.dtos.CitaDetailDTO;
import co.cue.citas_service.dtos.CitaRequestDTO;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.entity.Cita;

import java.time.LocalDate;
import java.util.List;

// Interfaz que define los métodos del servicio de citas
public interface ICitaService {

    // Buscar todas las citas por estado
    List<Cita> findCitaByEstado(String estado);

    // Buscar una cita por su ID y devolver DTO
    CitaResponseDTO findCitaById (Long id);

    // Crear una nueva cita
    CitaResponseDTO createCita(CitaRequestDTO citaDTO, Long usuarioId);

    // Actualizar una cita existente
    CitaUpdateDTO updateCita(Long id,CitaUpdateDTO citaUpdateDTO);

    // Cancelar o eliminar una cita
    void deleteCita(Long id);

    // Obtener todas las citas de un día específico
    List<CitaResponseDTO> findCitasDelDia(LocalDate fecha);

    List<CitaResponseDTO> getAllCitas();

    CitaDetailDTO getCitaDetailById(Long id);

    List<CitaDetailDTO> getAllCitasDetails();

    // Confirmar una cita mediante token (endpoint público)
    void confirmarCitaPorToken(String token);

}
