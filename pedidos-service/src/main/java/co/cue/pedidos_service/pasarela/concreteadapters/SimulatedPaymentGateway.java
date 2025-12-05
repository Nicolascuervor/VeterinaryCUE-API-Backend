package co.cue.pedidos_service.pasarela.concreteadapters;

import co.cue.pedidos_service.pasarela.IPasarelaPagoGateway;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * SimulatedPaymentGateway
 *
 * Implementación simulada del gateway de pasarela de pagos para desarrollo y testing.
 * Esta clase simula completamente el proceso de pago sin necesidad de interactuar
 * con servicios externos como Stripe.
 *
 * Características:
 * - Genera clientSecrets simulados con formato similar a Stripe
 * - No requiere configuración de claves API
 * - Útil para desarrollo, testing y demostraciones
 * - Mantiene la misma interfaz que StripeAdapterService
 */
@Service
@Slf4j
public class SimulatedPaymentGateway implements IPasarelaPagoGateway {

    private static final String SIMULATED_PREFIX = "pi_simulated_";
    private static final String SECRET_SUFFIX = "_secret_simulated_";

    /**
     * Crea una intención de pago simulada.
     *
     * Genera un clientSecret con formato similar a Stripe pero con prefijos
     * que indican que es una simulación.
     *
     * @param total Monto total del pedido
     * @param pedidoId Identificador del pedido
     * @param moneda Código de moneda (no se usa en simulación, pero se mantiene por compatibilidad)
     * @return ClientSecret simulado con formato: pi_simulated_{pedidoId}_secret_simulated_{uuid}
     */
    @Override
    public String crearIntencionDePago(BigDecimal total, Long pedidoId, String moneda) {
        log.info("SIMULATED: Creando PaymentIntent simulado para Pedido ID: {}, Total: {} {}", 
                pedidoId, total, moneda);

        // Generar un clientSecret simulado con formato similar a Stripe
        String paymentIntentId = SIMULATED_PREFIX + pedidoId;
        String secretPart = SECRET_SUFFIX + UUID.randomUUID().toString().replace("-", "");
        String clientSecret = paymentIntentId + secretPart;

        log.info("SIMULATED: PaymentIntent creado - ID: {}", paymentIntentId);
        log.info("SIMULATED: ClientSecret generado (formato simulado)");

        return clientSecret;
    }

    /**
     * Procesa un evento de webhook simulado.
     *
     * En modo simulación, este método valida que el paymentIntentId tenga
     * el formato simulado y crea un EventoPagoDTO indicando que el pago fue exitoso.
     *
     * @param payload Cuerpo del evento (no se usa en simulación, pero se mantiene por compatibilidad)
     * @param firmaHeader Firma del evento (no se valida en simulación)
     * @return EventoPagoDTO con pagoExitoso = true
     * @throws PasarelaPagoException Si el paymentIntentId no tiene formato simulado válido
     */
    @Override
    public EventoPagoDTO procesarEventoWebhook(String payload, String firmaHeader) throws PasarelaPagoException {
        log.info("SIMULATED: Procesando Webhook simulado...");

        // En simulación, extraemos el pedidoId del paymentIntentId
        // El formato es: pi_simulated_{pedidoId}_secret_simulated_{uuid}
        // Para simular el webhook, necesitamos el paymentIntentId que viene en el payload
        // o en los metadatos. Por simplicidad, asumimos que el payload contiene el paymentIntentId
        
        try {
            // Extraer paymentIntentId del payload (formato JSON simple)
            // En una implementación real, esto vendría del webhook
            String paymentIntentId = extraerPaymentIntentIdDelPayload(payload);
            
            if (paymentIntentId == null || !paymentIntentId.startsWith(SIMULATED_PREFIX)) {
                log.warn("SIMULATED: PaymentIntent ID no válido o no es simulado: {}", paymentIntentId);
                throw new PasarelaPagoException("PaymentIntent ID no válido para simulación");
            }

            // Extraer pedidoId del paymentIntentId
            // Formato: pi_simulated_{pedidoId}_secret_simulated_{uuid}
            String pedidoIdStr = paymentIntentId.replace(SIMULATED_PREFIX, "")
                    .split("_")[0];
            Long pedidoId = Long.parseLong(pedidoIdStr);

            log.info("SIMULATED: Evento de pago simulado exitoso para Pedido ID: {}", pedidoId);

            return new EventoPagoDTO(
                    paymentIntentId,
                    "payment_intent.succeeded",
                    pedidoId,
                    true  // Siempre exitoso en simulación
            );
        } catch (Exception e) {
            log.error("SIMULATED: Error al procesar webhook simulado: {}", e.getMessage());
            throw new PasarelaPagoException("Error procesando webhook simulado: " + e.getMessage(), e);
        }
    }

    /**
     * Extrae el paymentIntentId del payload del webhook.
     * 
     * En modo simulación, el payload puede venir en diferentes formatos.
     * Este método intenta extraerlo de forma flexible.
     */
    private String extraerPaymentIntentIdDelPayload(String payload) {
        if (payload == null || payload.trim().isEmpty()) {
            return null;
        }

        // Si el payload es directamente el paymentIntentId
        if (payload.startsWith(SIMULATED_PREFIX)) {
            return payload.split("_secret_")[0];
        }

        // Si el payload es JSON, intentar extraer el paymentIntentId
        // Formato esperado: {"paymentIntentId": "pi_simulated_123_secret_..."}
        if (payload.contains("paymentIntentId")) {
            try {
                // Extracción simple de JSON (sin usar librerías pesadas)
                int startIndex = payload.indexOf("\"paymentIntentId\"");
                if (startIndex != -1) {
                    int colonIndex = payload.indexOf(":", startIndex);
                    int startQuote = payload.indexOf("\"", colonIndex) + 1;
                    int endQuote = payload.indexOf("\"", startQuote);
                    if (endQuote > startQuote) {
                        return payload.substring(startQuote, endQuote);
                    }
                }
            } catch (Exception e) {
                log.warn("SIMULATED: No se pudo extraer paymentIntentId del JSON: {}", e.getMessage());
            }
        }

        return null;
    }
}

