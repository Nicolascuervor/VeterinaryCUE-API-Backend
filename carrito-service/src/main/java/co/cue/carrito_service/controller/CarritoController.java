package co.cue.carrito_service.controller;

import co.cue.carrito_service.models.dtos.requestdtos.AddItemRequestDTO;
import co.cue.carrito_service.models.dtos.responsedtos.CarritoResponseDTO;
import co.cue.carrito_service.services.ICarritoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "*"}) // Configuración CORS local específica para este controlador
// Controlador que maneja las solicitudes HTTP para el carrito de compras
public class CarritoController {

    // Servicio de carrito inyectado
    private final ICarritoService carritoService;

    // Obtener el carrito del usuario o de la sesión
    @GetMapping
    public ResponseEntity<CarritoResponseDTO> getCarrito(
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        CarritoResponseDTO carrito = carritoService.getOrCreateCarrito(usuarioId, sessionId);
        return ResponseEntity.ok(carrito);
    }

    // Agregar un item al carrito
    @PostMapping("/items")
    public ResponseEntity<CarritoResponseDTO> addItemAlCarrito(
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @RequestBody AddItemRequestDTO itemDTO) {

        CarritoResponseDTO carritoActualizado = carritoService.addItem(usuarioId, sessionId, itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoActualizado);
    }

    /**
     * Elimina un item del carrito basado en su Producto ID.
     */
    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponseDTO> removeItemDelCarrito(
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @PathVariable Long productoId) {

        CarritoResponseDTO carritoActualizado = carritoService.removeItem(usuarioId, sessionId, productoId);
        return ResponseEntity.ok(carritoActualizado);
    }

    /**
     * Vacía todos los items del carrito.
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCarrito(
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        carritoService.clearCarrito(usuarioId, sessionId);
        return ResponseEntity.noContent().build();
    }

}