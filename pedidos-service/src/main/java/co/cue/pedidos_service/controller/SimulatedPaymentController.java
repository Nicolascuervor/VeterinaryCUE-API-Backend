package co.cue.pedidos_service.controller;

import co.cue.pedidos_service.models.dtos.responsedtos.SimulatedPaymentResponseDTO;
import co.cue.pedidos_service.models.entities.Pedido;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.repository.PedidoRepository;
import co.cue.pedidos_service.services.PedidoWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * SimulatedPaymentController
 *
 * Controlador para simular pagos exitosos en modo desarrollo/testing.
 * Permite completar un pedido simulando que el pago fue exitoso.
 *
 * IMPORTANTE: Este endpoint solo debe estar disponible cuando
 * payment.simulation.enabled=true
 */
@RestController
@RequestMapping("/api/pedidos/simulate")
@RequiredArgsConstructor
@Slf4j
public class SimulatedPaymentController {

    private final PedidoWebhookService pedidoWebhookService;
    private final PedidoRepository pedidoRepository;

    @Value("${payment.simulation.enabled:false}")
    private boolean simulationEnabled;

    /**
     * Simula un pago exitoso para un pedido.
     *
     * Este endpoint permite completar un pedido simulando que el pago fue exitoso.
     * Útil para desarrollo y testing sin necesidad de configurar Stripe.
     *
     * @param pedidoId ID del pedido a completar
     * @return Respuesta indicando si el pago simulado fue exitoso
     */
    @PostMapping("/payment/{pedidoId}")
    public ResponseEntity<SimulatedPaymentResponseDTO> simularPagoExitoso(
            @PathVariable Long pedidoId) {

        // Verificar que la simulación esté habilitada
        if (!simulationEnabled) {
            log.warn("Intento de usar endpoint de simulación cuando está deshabilitado");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new SimulatedPaymentResponseDTO(
                            false,
                            "La simulación de pagos está deshabilitada. Configure payment.simulation.enabled=true"
                    ));
        }

        try {
            log.info("SIMULATED: Simulando pago exitoso para Pedido ID: {}", pedidoId);

            // Buscar el pedido para obtener el paymentIntentId real guardado
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));

            String paymentIntentId = pedido.getStripePaymentIntentId();
            
            if (paymentIntentId == null || paymentIntentId.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new SimulatedPaymentResponseDTO(
                                false,
                                "El pedido no tiene un PaymentIntent asociado. Asegúrate de haber iniciado el checkout primero."
                        ));
            }

            EventoPagoDTO eventoPago = new EventoPagoDTO(
                    paymentIntentId,
                    "payment_intent.succeeded",
                    pedidoId,
                    true  // Pago exitoso
            );

            // Procesar el pago exitoso (descontar stock, actualizar estado, etc.)
            pedidoWebhookService.procesarPagoExitoso(eventoPago);

            log.info("SIMULATED: Pago simulado completado exitosamente para Pedido ID: {}", pedidoId);

            return ResponseEntity.ok(new SimulatedPaymentResponseDTO(
                    true,
                    "Pago simulado completado exitosamente. El pedido ha sido procesado."
            ));

        } catch (Exception e) {
            log.error("SIMULATED: Error al simular pago para Pedido ID {}: {}", pedidoId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SimulatedPaymentResponseDTO(
                            false,
                            "Error al simular el pago: " + e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint alternativo que acepta el paymentIntentId directamente.
     * Útil cuando el frontend ya tiene el clientSecret y quiere simular la confirmación.
     *
     * @param paymentIntentId ID del PaymentIntent (formato: pi_simulated_{pedidoId})
     * @return Respuesta indicando si el pago simulado fue exitoso
     */
    @PostMapping("/payment/confirm")
    public ResponseEntity<SimulatedPaymentResponseDTO> simularConfirmacionPago(
            @RequestParam String paymentIntentId) {

        if (!simulationEnabled) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new SimulatedPaymentResponseDTO(
                            false,
                            "La simulación de pagos está deshabilitada."
                    ));
        }

        try {
            log.info("SIMULATED: Simulando confirmación de pago para PaymentIntent: {}", paymentIntentId);

            // Extraer pedidoId del paymentIntentId
            // Formato: pi_simulated_{pedidoId}_secret_simulated_{uuid}
            if (!paymentIntentId.startsWith("pi_simulated_")) {
                return ResponseEntity.badRequest()
                        .body(new SimulatedPaymentResponseDTO(
                                false,
                                "PaymentIntent ID no válido para simulación. Debe empezar con 'pi_simulated_'"
                        ));
            }

            String pedidoIdStr = paymentIntentId.replace("pi_simulated_", "").split("_")[0];
            Long pedidoId = Long.parseLong(pedidoIdStr);

            EventoPagoDTO eventoPago = new EventoPagoDTO(
                    paymentIntentId,
                    "payment_intent.succeeded",
                    pedidoId,
                    true
            );

            pedidoWebhookService.procesarPagoExitoso(eventoPago);

            return ResponseEntity.ok(new SimulatedPaymentResponseDTO(
                    true,
                    "Pago simulado confirmado exitosamente."
            ));

        } catch (Exception e) {
            log.error("SIMULATED: Error al simular confirmación de pago: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SimulatedPaymentResponseDTO(
                            false,
                            "Error al simular la confirmación: " + e.getMessage()
                    ));
        }
    }
}

