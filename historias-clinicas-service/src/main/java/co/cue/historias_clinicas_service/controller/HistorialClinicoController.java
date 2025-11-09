package co.cue.historias_clinicas_service.controller;

import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.service.IHistorialClinicoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-clinico")
@AllArgsConstructor

public class HistorialClinicoController {
    private final IHistorialClinicoService historialClinicoService;


    @GetMapping("/{id}")
    public ResponseEntity<HistorialClinicoResponseDTO> obtenerHistorialClinicoPorId(@PathVariable Long id) {
        HistorialClinicoResponseDTO historialClinico = historialClinicoService.findMedicalRecordByPetId(id);
        return ResponseEntity.ok(historialClinico);
    }

    @PostMapping
    public ResponseEntity<HistorialClinicoResponseDTO> crearHistorialClinico(@RequestBody HistorialClinicoRequestDTO requestDTO) {
        HistorialClinicoResponseDTO nuevaHistorialClinico = historialClinicoService.createHistorialMedico(requestDTO);
        return new ResponseEntity<>(nuevaHistorialClinico, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialClinicoResponseDTO> actualizarHistorialClinico(@PathVariable Long id, @RequestBody HistorialClinicoRequestDTO requestDTO) {
        HistorialClinicoResponseDTO actualizada = historialClinicoService.updateHistorialMedico(id, requestDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistorialClinico(@PathVariable Long id) {
        historialClinicoService.deleteHistorialMedico(id);
        return ResponseEntity.noContent().build();
    }
}
