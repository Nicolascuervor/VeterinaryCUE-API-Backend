package co.cue.pedidos_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO que se usa SOLO si el checkout es de un invitado (no logueado).
 * El frontend deberá capturar esta información.
 */
@Getter
@Setter
public class CheckoutGuestRequestDTO {
    private String clienteNombre;
    private String clienteEmail;
}