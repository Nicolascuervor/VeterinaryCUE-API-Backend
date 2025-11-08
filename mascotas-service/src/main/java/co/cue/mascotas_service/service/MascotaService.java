package co.cue.mascotas_service.service;

import co.cue.mascotas_service.dto.MascotaRequestDTO;
import co.cue.mascotas_service.dto.MascotaResponseDTO;
import co.cue.mascotas_service.model.Mascota;
import co.cue.mascotas_service.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    @Transactional
    public MascotaResponseDTO createMascota(MascotaRequestDTO requestDTO) {
        log.info("Creando nueva mascota: {}", requestDTO.getNombre());


        Mascota mascota = Mascota.builder()
                .nombre(requestDTO.getNombre())
                .especie(requestDTO.getEspecie())
                .raza(requestDTO.getRaza())
                .fechaNacimiento(requestDTO.getFechaNacimiento())
                .sexo(requestDTO.getSexo())
                .color(requestDTO.getColor())
                .peso(requestDTO.getPeso())
                .duenoId(requestDTO.getDueño_id())
                .active(true)
                .build();

        Mascota savedMascota = mascotaRepository.save(mascota);
        log.info("Mascota creada exitosamente con ID: {}", savedMascota.getId());

        return mapToResponseDTO(savedMascota);
    }

    @Transactional(readOnly = true)
    public MascotaResponseDTO getMascotaById(Long id) {
        log.info("Buscando mascota con ID: {}", id);
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
        return mapToResponseDTO(mascota);
    }

    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getAllMascotas() {
        log.info("Obteniendo todas las mascotas");
        return mascotaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getActiveMascotas() {
        log.info("Obteniendo mascotas activas");
        return mascotaRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getMascotasByOwner(Long ownerId) {
        log.info("Obteniendo mascotas del dueño con ID: {}", ownerId);
        return mascotaRepository.findByDuenoId(ownerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
/*
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getActiveMascotasByOwner(Long ownerId) {
        log.info("Obteniendo mascotas activas del dueño con ID: {}", ownerId);
        return mascotaRepository.findBydueño_idAfterAndActiveTrue(ownerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }*/

    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> searchMascotasByName(String name) {
        log.info("Buscando mascotas con nombre: {}", name);
        return mascotaRepository.searchByName(name).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO requestDTO) {
        log.info("Actualizando mascota con ID: {}", id);

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));


        mascota.setNombre(requestDTO.getNombre());
        mascota.setEspecie(requestDTO.getEspecie());
        mascota.setRaza(requestDTO.getRaza());
        mascota.setFechaNacimiento(requestDTO.getFechaNacimiento());
        mascota.setSexo(requestDTO.getSexo());
        mascota.setColor(requestDTO.getColor());
        mascota.setPeso(requestDTO.getPeso());


        Mascota updatedMascota = mascotaRepository.save(mascota);
        log.info("Mascota actualizada exitosamente");

        return mapToResponseDTO(updatedMascota);
    }

    @Transactional
    public void deactivateMascota(Long id) {
        log.info("Desactivando mascota con ID: {}", id);
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
        mascota.setActive(false);
        mascotaRepository.save(mascota);
        log.info("Mascota desactivada exitosamente");
    }

    @Transactional
    public void deleteMascota(Long id) {
        log.info("Eliminando mascota con ID: {}", id);
        if (!mascotaRepository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada con ID: " + id);
        }
        mascotaRepository.deleteById(id);
        log.info("Mascota eliminada exitosamente");
    }

    private MascotaResponseDTO mapToResponseDTO(Mascota mascota) {
        return MascotaResponseDTO.builder()
                .id(mascota.getId())
                .nombre(mascota.getNombre())
                .especie(mascota.getEspecie())
                .raza(mascota.getRaza())
                .fechaNacimiento(mascota.getFechaNacimiento())
                .dato(calculateAge(mascota.getFechaNacimiento()))
                .sexo(mascota.getSexo())
                .color(mascota.getColor())
                .peso(mascota.getPeso())
                .dueño_id(mascota.getDuenoId())
                .active(mascota.getActive())
                .build();

    }

    private Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) return null;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}