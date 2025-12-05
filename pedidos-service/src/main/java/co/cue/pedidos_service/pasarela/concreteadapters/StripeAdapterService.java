package co.cue.pedidos_service.pasarela.concreteadapters;

import co.cue.pedidos_service.pasarela.IPasarelaPagoGateway;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
/**
 * StripeAdapterService
 *
 * Implementación concreta del gateway de pasarela de pagos utilizando la API de Stripe.
 * Esta clase funciona como un "Adapter" dentro del patrón de arquitectura Hexagonal o Ports & Adapters.
 *
 * Responsabilidades:
 * - Crear PaymentIntent en Stripe para iniciar el proceso de pago.
 * - Validar y procesar eventos enviados por Stripe mediante Webhooks.
 * - Convertir los datos de Stripe en un modelo interno (EventoPagoDTO) que pueda ser
 *   utilizado por la capa de aplicación sin depender directamente del SDK de Stripe.
 *
 * Esta clase garantiza el aislamiento del negocio respecto a la tecnología de pago.
 */
@Service
@Slf4j
public class StripeAdapterService implements IPasarelaPagoGateway {
    /**
     * Clave secreta privada de Stripe para realizar solicitudes autenticadas.
     * Es inyectada desde el archivo de configuración.
     */
    @Value("${stripe.api.secret-key}")
    private String secretKey;
    /**
     * Clave secreta usada para validar la firma de los Webhooks recibidos desde Stripe.
     * Asegura que los eventos provienen realmente de Stripe.
     */
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;


    /**
     * Crear un PaymentIntent en Stripe.
     *
     * Este método es invocado desde la capa de aplicación para iniciar el proceso de pago.
     * Convierte el monto total del pedido a centavos, envía los datos a Stripe y recibe
     * un clientSecret que luego será usado por el frontend para completar el pago.
     *
     *  Monto total del pedido
     * Identificador del pedido generado en el sistema
     * Código de moneda (ej. "usd", "cop")
     *  ClientSecret del PaymentIntent
     */
    @Override
    public String crearIntencionDePago(BigDecimal total, Long pedidoId, String moneda) {
        log.info("ADAPTER: Creando PaymentIntent en Stripe para Pedido ID: {}", pedidoId);

        long amountInCents = total.multiply(new BigDecimal(100)).longValue();
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amountInCents);
        params.put("currency", moneda);
        params.put("metadata", Map.of("pedido_id", pedidoId.toString()));

        RequestOptions options = RequestOptions.builder()
                .setApiKey(secretKey)
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params, options);
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            log.error("Error al crear PaymentIntent de Stripe: {}", e.getMessage());
            throw new PasarelaPagoException("Error al contactar la pasarela de pago", e);
        }
    }

    /**
     * Procesa un evento enviado por Stripe a través de un webhook.
     *
     * Valida la firma del evento, lo deserializa y extrae los datos relevantes
     * para la lógica del negocio, retornando un EventoPagoDTO que representa
     * la información necesaria sin exponer detalles del SDK externo.
     *
     * Cuerpo del evento enviado por Stripe
     * Objeto EventoPagoDTO con información normalizada del evento
     * Si la firma es inválida o el evento es ilegible
     */
    @Override
    public EventoPagoDTO procesarEventoWebhook(String payload, String firmaHeader) throws PasarelaPagoException {
        log.info("ADAPTER: Procesando Webhook de Stripe...");
        Event event;

        try {
            event = Webhook.constructEvent(payload, firmaHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.warn("¡Firma de Webhook INVÁLIDA! {}", e.getMessage());
            throw new PasarelaPagoException("Firma de webhook inválida", e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar payload de Webhook: {}", e.getMessage());
            throw new PasarelaPagoException("Error procesando webhook", e);
        }

        StripeObject stripeObject = event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        String eventType = event.getType();
        boolean pagoExitoso = false;
        String paymentIntentId = null;
        Long pedidoId = null;
        if ("payment_intent.succeeded".equals(eventType)) {
            if (stripeObject instanceof PaymentIntent paymentIntent) {
                pagoExitoso = true;
                paymentIntentId = paymentIntent.getId();

                // Recuperamos el ID del pedido que guardamos en los metadatos al crear el pago
                String pedidoIdStr = paymentIntent.getMetadata().get("pedido_id");
                if (pedidoIdStr != null) {
                    pedidoId = Long.parseLong(pedidoIdStr);
                }
                log.info("ADAPTER: Evento 'payment_intent.succeeded' recibido para Pedido ID: {}", pedidoId);
            }
        } else {
            log.info("ADAPTER: Evento de Stripe recibido [{}], pero no es 'succeeded'. Ignorando.", eventType);
        }

        return new EventoPagoDTO(paymentIntentId, eventType, pedidoId, pagoExitoso);
    }
}
