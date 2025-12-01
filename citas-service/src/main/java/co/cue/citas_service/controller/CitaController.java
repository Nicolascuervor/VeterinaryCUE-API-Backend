package co.cue.citas_service.controller;

import co.cue.citas_service.dtos.CitaRequestDTO;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.service.ICitaService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // Indica que esta clase expone endpoints REST
@RequestMapping("/api/citas") // Ruta base para todos los endpoints de citas
@AllArgsConstructor // Inyecta las dependencias mediante constructor
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "*"}) // Configuración CORS local específica para este controlador
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
            @RequestBody CitaRequestDTO requestDTO,   // Datos de la cita
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) { // <-- ¡LA SOLUCIÓN!

        // Llama al servicio para crear la cita
        CitaResponseDTO nuevaCita = citaService.createCita(requestDTO, usuarioId);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED); // Retorna la cita creada con status 201
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
    
}
