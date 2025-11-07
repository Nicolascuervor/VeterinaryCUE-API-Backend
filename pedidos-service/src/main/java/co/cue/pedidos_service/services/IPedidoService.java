package co.cue.pedidos_service.services;

import co.cue.pedidos_service.models.dtos.requestdtos.CheckoutGuestRequestDTO;
import co.cue.pedidos_service.models.dtos.responsedtos.CheckoutResponseDTO;

public interface IPedidoService {

    /**
     *  Se orquesta el proceso de checkout.
     * 1. Obtiene datos del cliente (Auth/Request)
     * 2. Obtiene el carrito (Carrito)
     * 3. Valida productos y stock (Inventario)
     * 4. Crea el Pedido local en estado PENDIENTE
     * 5. Crea la Intención de Pago (Stripe)

     */
    CheckoutResponseDTO iniciarCheckout(Long usuarioId, String sessionId, CheckoutGuestRequestDTO guestDTO);

}
