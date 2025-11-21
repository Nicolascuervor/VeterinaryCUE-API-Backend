package co.cue.pedidos_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckoutResponseDTO {
    private Long pedidoId;
    private String clientSecret;
}
