package co.cue.pedidos_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SimulatedPaymentResponseDTO
 *
 * DTO para la respuesta de un pago simulado.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimulatedPaymentResponseDTO {
    /**
     * Indica si el pago simulado fue exitoso.
     */
    private boolean success;

    /**
     * Mensaje descriptivo del resultado.
     */
    private String message;
}

