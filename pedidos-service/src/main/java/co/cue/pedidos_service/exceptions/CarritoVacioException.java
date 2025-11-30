package co.cue.pedidos_service.exceptions;
// Clase que representa una excepción personalizada cuando el carrito está vacío
public class CarritoVacioException extends RuntimeException {
    // Constructor que recibe un mensaje y lo envía a la clase padre RuntimeException
    public CarritoVacioException(String message) {
        super(message);  // Llama al constructor de RuntimeException con el mensaje recibido
    }
}
