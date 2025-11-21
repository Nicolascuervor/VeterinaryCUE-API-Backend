package co.cue.pedidos_service.controller;

import co.cue.pedidos_service.models.dtos.requestdtos.CheckoutGuestRequestDTO;
import co.cue.pedidos_service.models.dtos.responsedtos.CheckoutResponseDTO;
import co.cue.pedidos_service.services.IPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@AllArgsConstructor
public class PedidoController {
    private final IPedidoService pedidoService;
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> iniciarCheckout(
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @RequestBody(required = false) CheckoutGuestRequestDTO guestDTO) {

        CheckoutResponseDTO response = pedidoService.iniciarCheckout(usuarioId, sessionId, guestDTO);

        return ResponseEntity.ok(response);
    }

}
