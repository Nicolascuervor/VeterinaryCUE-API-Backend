package co.cue.pedidos_service.services;

import co.cue.pedidos_service.models.dtos.requestdtos.CheckoutGuestRequestDTO;
import co.cue.pedidos_service.models.dtos.responsedtos.CheckoutResponseDTO;

public interface IPedidoService {
    CheckoutResponseDTO iniciarCheckout(Long usuarioId, String sessionId, CheckoutGuestRequestDTO guestDTO);

}
