package co.cue.pedidos_service.pasarela.exceptions;

public class PasarelaPagoException extends RuntimeException {
    public PasarelaPagoException(String message) {
        super(message);
    }

    public PasarelaPagoException(String message, Throwable cause) {
        super(message, cause);
    }
}
