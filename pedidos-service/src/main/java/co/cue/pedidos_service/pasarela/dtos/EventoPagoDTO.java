package co.cue.pedidos_service.pasarela.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//Anotación Lombok que genera automáticamente los getters y setters para todos los atributos
// Anotación Lombok que genera un constructor con todos los argumentos
// Anotación Lombok que genera un constructor vacío (sin argumentos)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventoPagoDTO {
    // Identificador del Payment Intent generado por Stripe
    private String paymentIntentId;
    // Tipo de evento recibido desde Stripe (ej: "payment_intent.succeeded", "payment_intent.payment_failed")
    private String tipoEvento;
    // ID del pedido asociado a este evento de pago
    private Long pedidoId;
    // Indica si el pago fue exitoso (true) o fallido (false)
    private boolean pagoExitoso;
}
