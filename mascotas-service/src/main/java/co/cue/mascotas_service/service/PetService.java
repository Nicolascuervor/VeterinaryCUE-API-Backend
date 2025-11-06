package co.cue.mascotas_service.service;

import co.cue.mascotas_service.dto.PetRequestDTO;
import co.cue.mascotas_service.dto.PetResponseDTO;
import co.cue.mascotas_service.model.Pet;
import co.cue.mascotas_service.repository.PetRepository;
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
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public PetResponseDTO createPet(PetRequestDTO requestDTO) {
        log.info("Creando nueva mascota: {}", requestDTO.getName());

        if (requestDTO.getMicrochipNumber() != null &&
                petRepository.existsByMicrochipNumber(requestDTO.getMicrochipNumber())) {
            throw new RuntimeException("Ya existe una mascota con ese número de microchip");
        }

        Pet pet = Pet.builder()
                .name(requestDTO.getName())
                .species(requestDTO.getSpecies())
                .breed(requestDTO.getBreed())
                .birthDate(requestDTO.getBirthDate())
                .gender(requestDTO.getGender())
                .color(requestDTO.getColor())
                .weight(requestDTO.getWeight())
                .ownerId(requestDTO.getOwnerId())
                .microchipNumber(requestDTO.getMicrochipNumber())
                .medicalNotes(requestDTO.getMedicalNotes())
                .active(true)
                .build();

        Pet savedPet = petRepository.save(pet);
        log.info("Mascota creada exitosamente con ID: {}", savedPet.getId());

        return mapToResponseDTO(savedPet);
    }

    @Transactional(readOnly = true)
    public PetResponseDTO getPetById(Long id) {
        log.info("Buscando mascota con ID: {}", id);
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
        return mapToResponseDTO(pet);
    }

    @Transactional(readOnly = true)
    public List<PetResponseDTO> getAllPets() {
        log.info("Obteniendo todas las mascotas");
        return petRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PetResponseDTO> getActivePets() {
        log.info("Obteniendo mascotas activas");
        return petRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PetResponseDTO> getPetsByOwner(Long ownerId) {
        log.info("Obteniendo mascotas del dueño con ID: {}", ownerId);
        return petRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PetResponseDTO> getActivePetsByOwner(Long ownerId) {
        log.info("Obteniendo mascotas activas del dueño con ID: {}", ownerId);
        return petRepository.findByOwnerIdAndActiveTrue(ownerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PetResponseDTO> searchPetsByName(String name) {
        log.info("Buscando mascotas con nombre: {}", name);
        return petRepository.searchByName(name).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PetResponseDTO updatePet(Long id, PetRequestDTO requestDTO) {
        log.info("Actualizando mascota con ID: {}", id);

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));

        if (requestDTO.getMicrochipNumber() != null &&
                !requestDTO.getMicrochipNumber().equals(pet.getMicrochipNumber()) &&
                petRepository.existsByMicrochipNumber(requestDTO.getMicrochipNumber())) {
            throw new RuntimeException("Ya existe una mascota con ese número de microchip");
        }

        pet.setName(requestDTO.getName());
        pet.setSpecies(requestDTO.getSpecies());
        pet.setBreed(requestDTO.getBreed());
        pet.setBirthDate(requestDTO.getBirthDate());
        pet.setGender(requestDTO.getGender());
        pet.setColor(requestDTO.getColor());
        pet.setWeight(requestDTO.getWeight());
        pet.setMicrochipNumber(requestDTO.getMicrochipNumber());
        pet.setMedicalNotes(requestDTO.getMedicalNotes());

        Pet updatedPet = petRepository.save(pet);
        log.info("Mascota actualizada exitosamente");

        return mapToResponseDTO(updatedPet);
    }

    @Transactional
    public void deactivatePet(Long id) {
        log.info("Desactivando mascota con ID: {}", id);
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
        pet.setActive(false);
        petRepository.save(pet);
        log.info("Mascota desactivada exitosamente");
    }

    @Transactional
    public void deletePet(Long id) {
        log.info("Eliminando mascota con ID: {}", id);
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada con ID: " + id);
        }
        petRepository.deleteById(id);
        log.info("Mascota eliminada exitosamente");
    }

    private PetResponseDTO mapToResponseDTO(Pet pet) {
        return PetResponseDTO.builder()
                .id(pet.getId())
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .birthDate(pet.getBirthDate())
                .ageInYears(calculateAge(pet.getBirthDate()))
                .gender(pet.getGender())
                .color(pet.getColor())
                .weight(pet.getWeight())
                .ownerId(pet.getOwnerId())
                .microchipNumber(pet.getMicrochipNumber())
                .medicalNotes(pet.getMedicalNotes())
                .active(pet.getActive())
                .createdAt(pet.getCreatedAt())
                .updatedAt(pet.getUpdatedAt())
                .build();
    }

    private Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) return null;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}