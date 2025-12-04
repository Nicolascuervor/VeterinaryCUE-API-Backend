package co.cue.citas_service.controller;

import co.cue.citas_service.dtos.CitaDetailDTO;
import co.cue.citas_service.dtos.CitaRequestDTO;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.service.ICitaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Indica que esta clase expone endpoints REST
@RequestMapping("/api/citas") // Ruta base para todos los endpoints de citas
@AllArgsConstructor // Inyecta las dependencias mediante constructor
@Slf4j
public class CitaController {
    private final ICitaService citaService;

    // Obtener una cita por su ID
    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        // Llama al servicio para buscar la cita
        CitaResponseDTO cita = citaService.findCitaById(id);
        return ResponseEntity.ok(cita);  // Retorna la cita encontrada
    }

    // Crear una nueva cita
    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(
            @RequestBody CitaRequestDTO requestDTO,
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) {

        // --- 3. IMPLEMENTACIÓN DEL LOG DE ENTRADA ---
        log.info("🔔 [POST /api/citas] Solicitud recibida. Usuario (Header): {}, Mascota: {}, Veterinario: {}, Servicio: {}",
                usuarioId,
                requestDTO.getPetId(),
                requestDTO.getVeterinarianId(),
                requestDTO.getServicioId());

        // Llama al servicio para crear la cita
        CitaResponseDTO nuevaCita = citaService.createCita(requestDTO, usuarioId);

        // (Opcional) Log de éxito
        log.info("✅ Cita creada exitosamente. ID asignado: {}", nuevaCita.getId());

        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    // Actualizar información de una cita existente
    @PutMapping("/{id}")
    public ResponseEntity<CitaUpdateDTO> actualizarCita(@PathVariable Long id, @RequestBody CitaUpdateDTO updateDTO) {
        CitaUpdateDTO actualizada = citaService.updateCita(id, updateDTO);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar una cita (cancelarla)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.deleteCita(id);  // Cancela la cita
        return ResponseEntity.noContent().build(); // Retorna un 204 sin contenido
    }
    // Obtener todas las citas de un día específico
    @GetMapping("/del-dia")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasDelDia(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        // Busca las citas de la fecha indicada
        List<CitaResponseDTO> citas = citaService.findCitasDelDia(fecha);
        return ResponseEntity.ok(citas);  // Retorna la lista de citas
    }

    @GetMapping("/all")
    public ResponseEntity<List<CitaResponseDTO>> obtenerTodasLasCitas() {
        return ResponseEntity.ok(citaService.getAllCitas());
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<CitaDetailDTO> getCitaDetail(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.getCitaDetailById(id));
    }

    @GetMapping("/details")
    public ResponseEntity<List<CitaDetailDTO>> obtenerTodasLasCitasDetalladas() {
        return ResponseEntity.ok(citaService.getAllCitasDetails());
    }
    
    // Endpoint público para confirmar una cita mediante token (sin autenticación)
    @GetMapping("/public/confirmar/{token}")
    public ResponseEntity<Map<String, String>> confirmarCitaPorToken(@PathVariable String token) {
        log.info("Solicitud de confirmación de cita recibida con token: {}", token);
        citaService.confirmarCitaPorToken(token);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Cita confirmada exitosamente");
        response.put("estado", "CONFIRMADA");
        
        return ResponseEntity.ok(response);
    }
    
    // Obtener calendario completo de citas futuras/pendientes del veterinario autenticado
    @GetMapping("/veterinario/calendario")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCalendarioCompleto(
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) {
        log.info("Solicitud de calendario completo para veterinario ID: {}", usuarioId);
        List<CitaResponseDTO> citas = citaService.obtenerCitasFuturasPorVeterinario(usuarioId);
        return ResponseEntity.ok(citas);
    }
    
}
