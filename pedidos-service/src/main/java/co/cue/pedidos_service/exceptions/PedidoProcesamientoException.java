package co.cue.pedidos_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PedidoProcesamientoException extends RuntimeException {
    public PedidoProcesamientoException(String message, Throwable cause) {
        super(message, cause);
    }
}
