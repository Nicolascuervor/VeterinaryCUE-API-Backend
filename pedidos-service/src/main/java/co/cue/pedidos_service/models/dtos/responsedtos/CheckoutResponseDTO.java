package co.cue.pedidos_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO de respuesta que le dice al Frontend cómo
 * inicializar la pasarela de pago de Stripe.
 */
@Getter
@AllArgsConstructor
public class CheckoutResponseDTO {

    /**
     * El ID de nuestro Pedido (en nuestra BD), para referencia del frontend.
     */
    private Long pedidoId;

    /**
     * Es la clave que el frontend necesita para mostrar el modal de pago.
     */
    private String clientSecret;
}
