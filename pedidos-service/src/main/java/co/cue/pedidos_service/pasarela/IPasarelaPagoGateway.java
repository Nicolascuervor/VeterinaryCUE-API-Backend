package co.cue.pedidos_service.pasarela;

import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;

import java.math.BigDecimal;

/**
 * Interfaz que define el contrato para la integración con una pasarela de pagos.
 * Proporciona los métodos necesarios para crear intenciones de pago y procesar
 * eventos enviados por la pasarela mediante Webhooks.
 */
public interface IPasarelaPagoGateway {
    /**
     * Crea una intención de pago en la pasarela seleccionada.
     * Este método envía el total del pedido, su identificador y la moneda
     * con el fin de generar un Payment Intent u objeto equivalente.
     *
     *  Monto total del pedido en valor decimal.
     * Identificador único del pedido asociado al pago.
     *  Código ISO de la moneda a utilizar (ej. "usd", "cop").
     *  Client Secret o token generado por la pasarela para continuar el pago.
     */
    String crearIntencionDePago(BigDecimal total, Long pedidoId, String moneda);
    /**
     * Procesa el evento recibido desde la pasarela de pago por medio de un Webhook.
     * Este método valida la firma del evento, interpreta el contenido y retorna
     * un objeto {@link EventoPagoDTO} con la información del pago realizado.
     *
     * Cuerpo del evento enviado por la pasarela.
     * Firma proporcionada en los headers para validar la autenticidad.
     * Contiene información del evento procesado.
     * Si la firma es inválida, el evento no puede procesarse
     *                               o ocurre un error interno en la pasarela.
     */
    EventoPagoDTO procesarEventoWebhook(String payload, String firmaHeader) throws PasarelaPagoException;
}
