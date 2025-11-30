package co.cue.pedidos_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;
/**
 * CheckoutGuestRequestDTO
 *
 * DTO utilizado para manejar los datos de un usuario invitado (guest)
 * durante el proceso de checkout. Este objeto solo se usa cuando
 * el usuario no está autenticado y debe proporcionar información básica
 * de contacto.
 *
 * Propósito:
 * - Capturar el nombre y el correo del cliente invitado.
 * - Permitir procesar un pedido sin necesidad de registro previo.
 *
 * Atributos:
 * Nombre del cliente invitado.
 * Correo electrónico del cliente invitado.
 */
@Getter
@Setter
public class CheckoutGuestRequestDTO {
    /**
     * Nombre del cliente invitado que realiza el pedido.
     */
    private String clienteNombre;
    /**
     * Correo electrónico del cliente invitado.
     * Será utilizado para notificaciones o envío de recibos.
     */
    private String clienteEmail;
}