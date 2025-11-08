package co.cue.mascotas_service.controller;

import co.cue.mascotas_service.dto.MascotaRequestDTO;
import co.cue.mascotas_service.dto.MascotaResponseDTO;
import co.cue.mascotas_service.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@Slf4j
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping
    public ResponseEntity<MascotaResponseDTO> createMascota(@Valid @RequestBody MascotaRequestDTO requestDTO) {
        log.info("POST /api/Mascotas - Crear nueva mascota");
        MascotaResponseDTO response = mascotaService.createMascota(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> getMascotaById(@PathVariable Long id) {
        log.info("GET /api/Mascotas/{} - Obtener mascota por ID", id);
        MascotaResponseDTO response = mascotaService.getMascotaById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> getAllMascotas(
            @RequestParam(required = false) Boolean active) {
        log.info("GET /api/Mascotas - Obtener todas las mascotas");
        List<MascotaResponseDTO> response = active != null && active
                ? mascotaService.getActiveMascotas()
                : mascotaService.getAllMascotas();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<MascotaResponseDTO>> getMascotasByOwner(
            @PathVariable Long ownerId) {

        log.info("GET /api/mascotas/owner/{} - Obtener mascotas por dueño", ownerId);

        // (Mentor): Llamamos al método que ya existe en MascotaService
        List<MascotaResponseDTO> response = mascotaService.getMascotasByOwner(ownerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MascotaResponseDTO>> searchMascotasByName(
            @RequestParam String name) {
        log.info("GET /api/Mascotas/search?name={}", name);
        List<MascotaResponseDTO> response = mascotaService.searchMascotasByName(name);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> updateMascota(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequestDTO requestDTO) {
        log.info("PUT /api/Mascotas/{} - Actualizar mascota", id);
        MascotaResponseDTO response = mascotaService.updateMascota(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateMascota(@PathVariable Long id) {
        log.info("PATCH /api/Mascotas/{}/deactivate - Desactivar mascota", id);
        mascotaService.deactivateMascota(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMascota(@PathVariable Long id) {
        log.info("DELETE /api/Mascotas/{} - Eliminar mascota", id);
        mascotaService.deleteMascota(id);
        return ResponseEntity.noContent().build();
    }
}