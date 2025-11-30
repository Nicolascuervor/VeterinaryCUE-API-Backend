package co.cue.pedidos_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * CheckoutResponseDTO
 *
 * DTO utilizado para responder al cliente después de iniciar el proceso
 * de checkout. Este objeto contiene la información esencial para que el
 * frontend continúe con el flujo de pago (por ejemplo, con Stripe).
 *
 * Propósito:
 * - Enviar el ID del pedido recién creado.
 * - Entregar el clientSecret generado por la pasarela de pago, el cual
 *   permite al frontend finalizar el pago de forma segura.
 *
 * Atributos:
 * ID del pedido generado durante el checkout.
 * Token/identificador proporcionado por la pasarela de pagos
 *(como Stripe) necesario para completar el pago en el cliente.
 */
@Getter
@AllArgsConstructor
public class CheckoutResponseDTO {
    /**
     * Identificador único del pedido generado.
     */
    private Long pedidoId;
    /**
     * Cliente secreto utilizado por la pasarela de pagos para completar la transacción.
     */
    private String clientSecret;
}
