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
public class CarritoController {

    private final ICarritoService carritoService;

    /**
     * (Arquitecto): Este es el 'cerebro' de la API.
     * Identifica al cliente (invitado o registrado) y obtiene su carrito.
     *
     * @param usuarioId (Header) Inyectado por el API-Gateway si el usuario está logueado.
     * @param sessionId (Header) Enviado por el Frontend si el usuario es invitado.
     * @return El carrito (existente o recién creado).
     */
    @GetMapping
    public ResponseEntity<CarritoResponseDTO> getCarrito(
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        CarritoResponseDTO carrito = carritoService.getOrCreateCarrito(usuarioId, sessionId);
        return ResponseEntity.ok(carrito);
    }

    /**
     * Agrega un nuevo item al carrito o actualiza su cantidad si ya existe.
     */
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

    // (Mentor): Más adelante, agregaremos un endpoint 'POST /api/carrito/merge'
    // para manejar la fusión cuando un invitado inicie sesión.
    // Por ahora, nos enfocamos en el CRUD básico.
}