package co.cue.pedidos_service.controller;

import co.cue.pedidos_service.models.dtos.requestdtos.CheckoutGuestRequestDTO;
import co.cue.pedidos_service.models.dtos.responsedtos.CheckoutResponseDTO;
import co.cue.pedidos_service.services.IPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Indica que esta clase es un controlador REST de Spring
@RestController
// Define la ruta base para todos los endpoints de este controlador
@RequestMapping("/api/pedidos")
// Genera un constructor con todos los atributos necesarios (Lombok)
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "*"}) // Configuración CORS local específica para este controlador
public class PedidoController {
    // Servicio encargado de manejar la lógica del módulo de pedidos
    private final IPedidoService pedidoService;
    // Endpoint HTTP POST para iniciar el proceso de checkout
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> iniciarCheckout(
            // Header opcional que contiene el ID del usuario autenticado
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId,
            // Header opcional que identifica la sesión del cliente (para carritos anónimos)
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            // Cuerpo de la solicitud opcional, usado cuando el usuario es invitado (guest)
            @RequestBody(required = false) CheckoutGuestRequestDTO guestDTO) {

        // Llama al servicio para iniciar el checkout usando usuarioId, sessionId o guestDTO
        CheckoutResponseDTO response = pedidoService.iniciarCheckout(usuarioId, sessionId, guestDTO);

        // Retorna la respuesta del checkout con status 200 OK
        return ResponseEntity.ok(response);
    }

}
