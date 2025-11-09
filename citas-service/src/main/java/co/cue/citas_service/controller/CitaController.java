package co.cue.citas_service.controller;

import co.cue.citas_service.dto.CitaRequestDTO;
import co.cue.citas_service.dto.CitaResponseDTO;
import co.cue.citas_service.dto.CitaUpdateDTO;
import co.cue.citas_service.service.ICitaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cita")
@AllArgsConstructor
public class CitaController {
    private final ICitaService citaService;

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        CitaResponseDTO cita = citaService.findCitaById(id);
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(@RequestBody CitaRequestDTO requestDTO) {
        CitaResponseDTO nuevaCita = citaService.createCita(requestDTO);
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
    
}
