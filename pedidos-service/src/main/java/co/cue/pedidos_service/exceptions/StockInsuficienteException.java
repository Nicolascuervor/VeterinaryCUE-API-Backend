package co.cue.pedidos_service.exceptions;
// Clase que representa una excepción personalizada para indicar falta de stock
public class StockInsuficienteException extends RuntimeException {
    // Constructor que recibe un mensaje descriptivo del error
    public StockInsuficienteException(String message) {
        super(message); // Llama al constructor de la clase RuntimeException con el mensaje recibido
    }
}
