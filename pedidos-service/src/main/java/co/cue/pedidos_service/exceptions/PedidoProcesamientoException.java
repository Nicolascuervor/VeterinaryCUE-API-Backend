package co.cue.pedidos_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
// Indica que cuando esta excepción sea lanzada, Spring responderá automáticamente con HTTP 500
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
// Constructor que recibe un mensaje y la causa original del error
public class PedidoProcesamientoException extends RuntimeException {
    public PedidoProcesamientoException(String message, Throwable cause) {
        super(message, cause); // Llama al constructor de RuntimeException pasando mensaje y causa
    }
}
