package co.cue.pedidos_service.pasarela.concreteadapters;


import co.cue.pedidos_service.pasarela.IPasarelaPagoGateway;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;
import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StripeAdapterService implements IPasarelaPagoGateway {

    @Value("${stripe.api.secret-key}")
    private String secretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;


    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /**
     * Implementación del método para crear un pago.
     */
    @Override
    public String crearIntencionDePago(BigDecimal total, Long pedidoId, String moneda) {
        log.info("ADAPTER: Creando PaymentIntent en Stripe para Pedido ID: {}", pedidoId);
        long amountInCents = total.multiply(new BigDecimal(100)).longValue();
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amountInCents);
        params.put("currency", moneda);
        params.put("metadata", Map.of("pedido_id", pedidoId.toString()));
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            log.error("Error al crear PaymentIntent de Stripe: {}", e.getMessage());
            throw new PasarelaPagoException("Error al contactar la pasarela de pago", e);
        }
    }

    /**
     * Implementación del método para procesar webhooks.
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
        } catch (JsonSyntaxException e) {
            log.error("Error al parsear payload de Webhook: {}", e.getMessage());
            throw new PasarelaPagoException("Payload de webhook malformado", e);
        }

        StripeObject stripeObject = event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new PasarelaPagoException("No se encontró 'data.object' en el evento de Stripe"));

        String eventType = event.getType();
        boolean pagoExitoso = false;
        String paymentIntentId = null;
        Long pedidoId = null;

        if ("payment_intent.succeeded".equals(eventType)) {
            if (stripeObject instanceof PaymentIntent paymentIntent) {
                pagoExitoso = true;
                paymentIntentId = paymentIntent.getId();
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
