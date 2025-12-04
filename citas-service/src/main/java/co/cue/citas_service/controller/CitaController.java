package co.cue.citas_service.controller;

import co.cue.citas_service.dtos.CitaDetailDTO;
import co.cue.citas_service.dtos.CitaRequestDTO;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.dtos.CitaConfirmacionResponseDTO;
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
    
    // Endpoint público para obtener información de una cita por token (sin confirmarla)
    @GetMapping("/public/informacion/{token}")
    public ResponseEntity<CitaConfirmacionResponseDTO> obtenerInformacionCitaPorToken(@PathVariable String token) {
        log.info("Solicitud de información de cita recibida con token: {}", token);
        CitaConfirmacionResponseDTO informacion = citaService.obtenerInformacionCitaPorToken(token);
        return ResponseEntity.ok(informacion);
    }
    
    // Endpoint público para confirmar una cita mediante token (sin autenticación)
    @GetMapping("/public/confirmar/{token}")
    public ResponseEntity<CitaConfirmacionResponseDTO> confirmarCitaPorToken(@PathVariable String token) {
        log.info("Solicitud de confirmación de cita recibida con token: {}", token);
        CitaConfirmacionResponseDTO respuesta = citaService.confirmarCitaPorToken(token);
        return ResponseEntity.ok(respuesta);
    }
    
    // Obtener calendario completo de citas futuras/pendientes del veterinario autenticado
    // Parámetro opcional 'estado' para filtrar por un estado específico
    @GetMapping("/veterinario/calendario")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCalendarioCompleto(
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId,
            @RequestParam(value = "estado", required = false) String estado) {
        log.info("Solicitud de calendario completo para veterinario ID: {}, estado: {}", usuarioId, estado);
        
        List<CitaResponseDTO> citas;
        if (estado != null && !estado.isEmpty()) {
            // Filtrar por estado específico (solo citas futuras)
            citas = citaService.obtenerCitasFuturasPorVeterinarioYEstado(usuarioId, estado);
        } else {
            // Sin filtro de estado (todas las citas futuras/pendientes)
            citas = citaService.obtenerCitasFuturasPorVeterinario(usuarioId);
        }
        
        return ResponseEntity.ok(citas);
    }
    
    // Obtener todas las citas del veterinario (pasadas y futuras)
    // Parámetro opcional 'estado' para filtrar por un estado específico
    @GetMapping("/veterinario/todas")
    public ResponseEntity<List<CitaResponseDTO>> obtenerTodasLasCitasPorVeterinario(
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId,
            @RequestParam(value = "estado", required = false) String estado) {
        log.info("Solicitud de todas las citas para veterinario ID: {}, estado: {}", usuarioId, estado);
        
        List<CitaResponseDTO> citas;
        if (estado != null && !estado.isEmpty()) {
            // Filtrar por estado específico (todas las citas, pasadas y futuras)
            citas = citaService.obtenerCitasPorVeterinarioYEstado(usuarioId, estado);
        } else {
            // Sin filtro de estado (todas las citas)
            citas = citaService.obtenerTodasLasCitasPorVeterinario(usuarioId);
        }
        
        return ResponseEntity.ok(citas);
    }
    
    // Endpoints específicos por estado para facilitar el uso
    @GetMapping("/veterinario/estado/{estado}")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasPorEstado(
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId,
            @PathVariable String estado,
            @RequestParam(value = "soloFuturas", defaultValue = "false") boolean soloFuturas) {
        log.info("Solicitud de citas para veterinario ID: {}, estado: {}, soloFuturas: {}", 
                usuarioId, estado, soloFuturas);
        
        List<CitaResponseDTO> citas;
        if (soloFuturas) {
            citas = citaService.obtenerCitasFuturasPorVeterinarioYEstado(usuarioId, estado);
        } else {
            citas = citaService.obtenerCitasPorVeterinarioYEstado(usuarioId, estado);
        }
        
        return ResponseEntity.ok(citas);
    }
    
}
