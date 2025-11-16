package co.cue.pedidos_service.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@Slf4j
public class StripeService {

    public String crearPaymentIntent(BigDecimal total, Long pedidoId) {

        log.info("[STRIPE STUB] Creando PaymentIntent para Pedido {} por {}", pedidoId, total);

        return "pi_" + pedidoId + "_secret_MOCK" + System.currentTimeMillis();
    }
}
