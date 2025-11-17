package co.cue.pedidos_service.pasarela;

import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;

import java.math.BigDecimal;

//  Interfaz "Target" (Objetivo).
//  Define las operaciones de pago que nuestro sistema necesita,
//  de forma genérica, sin acoplarse a Stripe, PayPal, o cualquier otro.

public interface IPasarelaPagoGateway {
    String crearIntencionDePago(BigDecimal total, Long pedidoId, String moneda);
    EventoPagoDTO procesarEventoWebhook(String payload, String firmaHeader) throws PasarelaPagoException;
}
