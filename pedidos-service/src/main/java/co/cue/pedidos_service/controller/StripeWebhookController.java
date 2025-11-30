package co.cue.pedidos_service.controller;

import co.cue.pedidos_service.pasarela.IPasarelaPagoGateway;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;
import co.cue.pedidos_service.services.PedidoWebhookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Indica que esta clase es un controlador REST dentro de Spring
@RestController
// Define la ruta base del Webhook de Stripe
@RequestMapping("/api/pedidos/stripe/webhook")

// Genera un constructor con los atributos necesarios mediante Lombok
@AllArgsConstructor
// Habilita el uso del logger para registrar información y errores
@Slf4j
public class StripeWebhookController {
    // Gateway encargado de comunicarse con la pasarela de pagos (Stripe)
    private final IPasarelaPagoGateway pasarelaPagoGateway;
    // Servicio que gestiona la lógica de negocio cuando un pago es exitoso
    private final PedidoWebhookService webhookService;

    // Endpoint HTTP POST que recibe los eventos de Stripe
    @PostMapping
    public ResponseEntity<Void> handleStripeWebhook(
            // Cuerpo del webhook enviado por Stripe en formato JSON
            @RequestBody String payload,
            // Cabecera que contiene la firma para validar que el evento proviene de Stripe
            @RequestHeader("Stripe-Signature") String stripeSignature) {

        // Log de entrada indicando que se recibió un webhook
        log.info("Webhook de Stripe recibido.");
        EventoPagoDTO evento;

        try {
            // Intenta procesar y validar el webhook desde la pasarela de pagos
            evento = pasarelaPagoGateway.procesarEventoWebhook(payload, stripeSignature);

        } catch (PasarelaPagoException e) {
            // Error en la validación o parsing del evento
            log.error("Error al procesar Webhook (Adapter): {}", e.getMessage());
            // Retorna un 400 Bad Request indicando que Stripe envió un evento inválido
            return ResponseEntity.badRequest().build();
        }

        try {
            // Ejecuta la lógica de negocio después de un pago exitoso
            webhookService.procesarPagoExitoso(evento);

        } catch (Exception e) {
            // Error interno al procesar el evento dentro del servicio
            log.error("Error en lógica de negocio post-pago: {}", e.getMessage());
            // Retorna 500 Internal Server Error indicando fallo del servidor
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}