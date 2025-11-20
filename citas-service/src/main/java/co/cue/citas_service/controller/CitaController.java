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

@RestController
@RequestMapping("/api/citas")
@AllArgsConstructor
public class CitaController {
    private final ICitaService citaService;

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        CitaResponseDTO cita = citaService.findCitaById(id);
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(
            @RequestBody CitaRequestDTO requestDTO,
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) { // <-- ¡LA SOLUCIÓN!
        CitaResponseDTO nuevaCita = citaService.createCita(requestDTO, usuarioId);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaUpdateDTO> actualizarCita(@PathVariable Long id, @RequestBody CitaUpdateDTO updateDTO) {
        CitaUpdateDTO actualizada = citaService.updateCita(id, updateDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/del-dia")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasDelDia(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<CitaResponseDTO> citas = citaService.findCitasDelDia(fecha);
        return ResponseEntity.ok(citas);
    }
    
}
