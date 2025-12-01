package co.cue.historias_clinicas_service.controller;

import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.entity.Reporte;
import co.cue.historias_clinicas_service.service.IHistorialClinicoService;
import co.cue.historias_clinicas_service.service.ReporteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-clinico")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "*"}) // Configuración CORS local específica para este controlador
public class HistorialClinicoController {

    private final IHistorialClinicoService historialClinicoService;
    private final ReporteService reporteService;

    /**
     * Obtener todos los historiales de una mascota validando permisos según el usuario.
     */

    @GetMapping("/mascota/{petId}")
    public ResponseEntity<List<HistorialClinicoResponseDTO>> obtenerHistorialesPorMascotaId(
            @PathVariable Long petId,
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) {
        List<HistorialClinicoResponseDTO> historiales = historialClinicoService.findMedicalRecordsByPetId(petId, usuarioId);
        return ResponseEntity.ok(historiales);
    }
    /**
     * Obtener un historial clínico específico.
     */
    @GetMapping("/{historialId}")
    public ResponseEntity<HistorialClinicoResponseDTO> obtenerHistorialClinicoPorId(
            @PathVariable Long historialId,
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) {

        HistorialClinicoResponseDTO historial = historialClinicoService.findMedicalRecordById(historialId, usuarioId);
        return ResponseEntity.ok(historial);
    }

    /**
     * Crear un historial clínico (solo veterinarios).
     */

    @PostMapping
    public ResponseEntity<HistorialClinicoResponseDTO> crearHistorialClinico(
            @RequestBody HistorialClinicoRequestDTO requestDTO,
            @RequestHeader(value = "X-Usuario-Id") Long veterinarioId) {

        HistorialClinicoResponseDTO nuevoHistorial = historialClinicoService.createHistorialMedico(requestDTO, veterinarioId);
        return new ResponseEntity<>(nuevoHistorial, HttpStatus.CREATED);
    }

    /**
     * Actualizar historial clínico (veterinario/administrador).
     */
    @PutMapping("/{historialId}")
    public ResponseEntity<HistorialClinicoResponseDTO> actualizarHistorialClinico(
            @PathVariable Long historialId,
            @RequestBody HistorialClinicoRequestDTO requestDTO,
            @RequestHeader(value = "X-Usuario-Id") Long veterinarioId) {

        HistorialClinicoResponseDTO actualizado = historialClinicoService.updateHistorialMedico(historialId, requestDTO, veterinarioId);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Eliminar historial clínico.
     */
    @DeleteMapping("/{historialId}")
    public ResponseEntity<Void> eliminarHistorialClinico(
            @PathVariable Long historialId,
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) {

        historialClinicoService.deleteHistorialMedico(historialId, usuarioId);
        return ResponseEntity.noContent().build();
    }
    /**
     * Generar reporte en texto plano.
     */
    @GetMapping(value = "/{historialId}/reporte", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> obtenerReporteDeHistorial(
            @PathVariable Long historialId,
            @RequestHeader(value = "X-Usuario-Id") Long usuarioId) {

        historialClinicoService.findMedicalRecordById(historialId, usuarioId);

        Reporte reporte = reporteService.generarReporteDeHistorial(historialId, usuarioId);

        return ResponseEntity.ok(reporte.toString());
    }
}