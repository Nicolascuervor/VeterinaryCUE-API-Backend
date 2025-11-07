package co.cue.pedidos_service.services;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class StripeService {

    public String crearPaymentIntent(BigDecimal total, Long pedidoId) {

        System.out.println("[STRIPE STUB] Creando PaymentIntent para Pedido " + pedidoId + " por " + total);

        return "pi_" + pedidoId + "_secret_MOCK" + System.currentTimeMillis();
    }
}
