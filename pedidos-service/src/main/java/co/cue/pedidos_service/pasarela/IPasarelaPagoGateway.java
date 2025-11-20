package co.cue.pedidos_service.pasarela;

import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;

import java.math.BigDecimal;


public interface IPasarelaPagoGateway {
    String crearIntencionDePago(BigDecimal total, Long pedidoId, String moneda);
    EventoPagoDTO procesarEventoWebhook(String payload, String firmaHeader) throws PasarelaPagoException;
}
