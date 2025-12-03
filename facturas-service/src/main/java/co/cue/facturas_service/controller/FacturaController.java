package co.cue.facturas_service.controller;

import co.cue.facturas_service.models.dtos.FacturaResponseDTO;
import co.cue.facturas_service.services.FacturaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// Indica que esta clase es un controlador REST de Spring
// y que cada método devuelve datos en formato JSON.
@RestController

// Define la ruta base para todos los endpoints de este controlador.
@RequestMapping("/api/facturas")

// Genera automáticamente un constructor con todos los atributos final.
@AllArgsConstructor
public class FacturaController {
    // Servicio encargado de la lógica de negocio relacionada con Facturas
    private final FacturaService facturaService;


    // ENDPOINT: Obtener todas las facturas
    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> listarTodas() {
        // Retorna una respuesta HTTP 200 OK con la lista de facturas.
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    // ENDPOINT: Obtener una factura por su ID
    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerPorId(@PathVariable Long id) {
        // Extrae el ID de la URL y retorna la factura correspondiente.
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }

    // ENDPOINT: Obtener todas las facturas de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        // Extrae el ID del usuario de la URL y devuelve sus facturas.
        return ResponseEntity.ok(facturaService.listarPorUsuario(usuarioId));
    }
}