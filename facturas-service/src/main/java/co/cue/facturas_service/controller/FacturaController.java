package co.cue.facturas_service.controller;

import co.cue.facturas_service.models.dtos.FacturaResponseDTO;
import co.cue.facturas_service.services.FacturaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/facturas")
@AllArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(facturaService.listarPorUsuario(usuarioId));
    }
}