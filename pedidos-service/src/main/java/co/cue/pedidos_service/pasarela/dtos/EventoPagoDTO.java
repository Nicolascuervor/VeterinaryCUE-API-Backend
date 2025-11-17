package co.cue.pedidos_service.pasarela.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventoPagoDTO {
    private String paymentIntentId;
    private String tipoEvento;
    private Long pedidoId;
    private boolean pagoExitoso;
}
