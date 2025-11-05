package co.cue.mascotas_service.controller;

import co.cue.mascotas_service.dto.PetRequestDTO;
import co.cue.mascotas_service.dto.PetResponseDTO;
import co.cue.mascotas_service.service.PetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@Slf4j
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDTO> createPet(@Valid @RequestBody PetRequestDTO requestDTO) {
        log.info("POST /api/pets - Crear nueva mascota");
        PetResponseDTO response = petService.createPet(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getPetById(@PathVariable Long id) {
        log.info("GET /api/pets/{} - Obtener mascota por ID", id);
        PetResponseDTO response = petService.getPetById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> getAllPets(
            @RequestParam(required = false) Boolean active) {
        log.info("GET /api/pets - Obtener todas las mascotas");
        List<PetResponseDTO> response = active != null && active
                ? petService.getActivePets()
                : petService.getAllPets();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PetResponseDTO>> getPetsByOwner(
            @PathVariable Long ownerId,
            @RequestParam(required = false) Boolean active) {
        log.info("GET /api/pets/owner/{} - Obtener mascotas por dueño", ownerId);
        List<PetResponseDTO> response = active != null && active
                ? petService.getActivePetsByOwner(ownerId)
                : petService.getPetsByOwner(ownerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PetResponseDTO>> searchPetsByName(
            @RequestParam String name) {
        log.info("GET /api/pets/search?name={}", name);
        List<PetResponseDTO> response = petService.searchPetsByName(name);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @PathVariable Long id,
            @Valid @RequestBody PetRequestDTO requestDTO) {
        log.info("PUT /api/pets/{} - Actualizar mascota", id);
        PetResponseDTO response = petService.updatePet(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePet(@PathVariable Long id) {
        log.info("PATCH /api/pets/{}/deactivate - Desactivar mascota", id);
        petService.deactivatePet(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        log.info("DELETE /api/pets/{} - Eliminar mascota", id);
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}