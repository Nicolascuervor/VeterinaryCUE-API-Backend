package co.cue.pedidos_service.controller;

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

    private final PedidoWebhookService webhookService;


    @PostMapping
    public ResponseEntity<Void> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String stripeSignature) {

        log.info("Webhook de Stripe recibido.");

        // Delegamos toda la lógica (validación y procesamiento) al servicio de Webhook.
        try {
            webhookService.handleStripeEvent(payload, stripeSignature);
        } catch (Exception e) {
            // Si algo falla, le decimos a Stripe que fue un error (ej. 400) para que lo reintente.
            log.error("Error procesando Webhook de Stripe: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}