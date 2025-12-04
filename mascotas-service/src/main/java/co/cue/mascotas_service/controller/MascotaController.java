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
// Controlador encargado de manejar todas las operaciones relacionadas con mascotas.
public class MascotaController {
    // Servicio encargado de la lógica de negocio para mascotas.
    private final MascotaService mascotaService;

    @PostMapping
    // Crea una nueva mascota en el sistema.
    public ResponseEntity<MascotaResponseDTO> createMascota(@Valid @RequestBody MascotaRequestDTO requestDTO) {
        log.info("POST /api/Mascotas - Crear nueva mascota");
        MascotaResponseDTO response = mascotaService.createMascota(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/public/{id}")
    // Endpoint público para obtener información básica de una mascota por ID.
    // Utilizado para confirmación de citas y otros flujos públicos.
    public ResponseEntity<MascotaResponseDTO> getMascotaByIdPublico(@PathVariable Long id) {
        log.info("GET /api/mascotas/public/{} - Obtener mascota por ID (público)", id);
        MascotaResponseDTO response = mascotaService.getMascotaById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    // Obtiene una mascota por su ID.
    public ResponseEntity<MascotaResponseDTO> getMascotaById(@PathVariable Long id) {
        log.info("GET /api/Mascotas/{} - Obtener mascota por ID", id);
        MascotaResponseDTO response = mascotaService.getMascotaById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    // Obtiene todas las mascotas o solo las activas si se pasa el parámetro 'active=true'.
    public ResponseEntity<List<MascotaResponseDTO>> getAllMascotas(
            @RequestParam(required = false) Boolean active) {
        log.info("GET /api/Mascotas - Obtener todas las mascotas");
        List<MascotaResponseDTO> response = active != null && active
                ? mascotaService.getActiveMascotas()
                : mascotaService.getAllMascotas();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/owner/{ownerId}")
    // Obtiene todas las mascotas asociadas a un dueño específico.
    public ResponseEntity<List<MascotaResponseDTO>> getMascotasByOwner(
            @PathVariable Long ownerId) {

        log.info("GET /api/mascotas/owner/{} - Obtener mascotas por dueño", ownerId);


        List<MascotaResponseDTO> response = mascotaService.getMascotasByOwner(ownerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    // Busca mascotas por coincidencia de nombre.
    public ResponseEntity<List<MascotaResponseDTO>> searchMascotasByName(
            @RequestParam String name) {
        log.info("GET /api/Mascotas/search?name={}", name);
        List<MascotaResponseDTO> response = mascotaService.searchMascotasByName(name);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    // Actualiza completamente la información de una mascota.
    public ResponseEntity<MascotaResponseDTO> updateMascota(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequestDTO requestDTO) {
        log.info("PUT /api/Mascotas/{} - Actualizar mascota", id);
        MascotaResponseDTO response = mascotaService.updateMascota(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    // Desactiva una mascota sin eliminarla del sistema.
    public ResponseEntity<Void> deactivateMascota(@PathVariable Long id) {
        log.info("PATCH /api/Mascotas/{}/deactivate - Desactivar mascota", id);
        mascotaService.deactivateMascota(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    // Elimina lógicamente una mascota del sistema.
    public ResponseEntity<Void> deleteMascota(@PathVariable Long id) {
        log.info("DELETE /api/Mascotas/{} - Eliminar mascota", id);
        mascotaService.deleteMascota(id);
        return ResponseEntity.noContent().build();
    }
}