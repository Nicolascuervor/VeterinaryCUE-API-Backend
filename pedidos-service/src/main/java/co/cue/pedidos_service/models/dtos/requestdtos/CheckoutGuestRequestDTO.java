package co.cue.pedidos_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutGuestRequestDTO {
    private String clienteNombre;
    private String clienteEmail;
}