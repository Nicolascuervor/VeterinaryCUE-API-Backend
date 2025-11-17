package co.cue.pedidos_service.controller;

import co.cue.pedidos_service.pasarela.IPasarelaPagoGateway;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;
import co.cue.pedidos_service.services.PedidoWebhookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos/stripe/webhook")
@AllArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final IPasarelaPagoGateway pasarelaPagoGateway;
    private final PedidoWebhookService webhookService;

    @PostMapping
    public ResponseEntity<Void> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String stripeSignature) {

        log.info("Webhook de Stripe recibido.");
        EventoPagoDTO evento;

        try {
            evento = pasarelaPagoGateway.procesarEventoWebhook(payload, stripeSignature);

        } catch (PasarelaPagoException e) {
            log.error("Error al procesar Webhook (Adapter): {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        try {
            webhookService.procesarPagoExitoso(evento);

        } catch (Exception e) {
            log.error("Error en lógica de negocio post-pago: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }


        return ResponseEntity.ok().build();
    }
}