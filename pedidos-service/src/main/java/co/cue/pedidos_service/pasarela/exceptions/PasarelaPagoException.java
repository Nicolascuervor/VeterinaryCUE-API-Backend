package co.cue.pedidos_service.pasarela.exceptions;
// Clase personalizada de excepción que extiende RuntimeException
public class PasarelaPagoException extends RuntimeException {
    // Constructor que recibe un mensaje y lo envía a la clase padre RuntimeException
    public PasarelaPagoException(String message) {
        super(message);
    }
    // Constructor que recibe un mensaje y una causa (otra excepción) y los envía a RuntimeException
    public PasarelaPagoException(String message, Throwable cause) {
        super(message, cause);
    }
}
