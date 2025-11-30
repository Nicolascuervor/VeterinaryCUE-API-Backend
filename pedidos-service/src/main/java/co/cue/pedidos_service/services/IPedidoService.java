package co.cue.pedidos_service.services;

import co.cue.pedidos_service.models.dtos.requestdtos.CheckoutGuestRequestDTO;
import co.cue.pedidos_service.models.dtos.responsedtos.CheckoutResponseDTO;
/**
 * Interfaz que define las operaciones relacionadas con la gestión de pedidos.
 * <p>
 * Esta capa representa la lógica de negocio encargada del flujo de creación
 * y procesamiento de un checkout, tanto para usuarios autenticados como
 * para clientes invitados (guest checkout).
 */
public interface IPedidoService {
    /**
     * Inicia el proceso de checkout de un pedido.
     * <p>
     * Este flujo puede ser usado tanto por clientes registrados (cuando se pasa
     * el usuarioId) como por clientes invitados (cuando se envía información
     * adicional en el {@link CheckoutGuestRequestDTO}).
     *
     * @param usuarioId ID del usuario autenticado (puede ser null si es un invitado).
     * @param sessionId ID de la sesión del carrito del cliente.
     * @param guestDTO Datos del cliente invitado (solo se usa si usuarioId es null).
     * @return {@link CheckoutResponseDTO} que contiene información necesaria
     *         para completar el pago, como el clientSecret de Stripe.
     */
    CheckoutResponseDTO iniciarCheckout(Long usuarioId, String sessionId, CheckoutGuestRequestDTO guestDTO);

}
