package co.cue.pedidos_service.config;

import co.cue.pedidos_service.exceptions.CarritoVacioException;
import co.cue.pedidos_service.exceptions.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// Indica que esta clase manejará excepciones globales en toda la aplicación
@ControllerAdvice
public class GlobalExceptionHandler {

    // Maneja excepciones relacionadas con errores de lógica del lado del cliente
    // 400 Bad Request - Cuando no hay suficiente stock o el carrito está vacío
    @ExceptionHandler({StockInsuficienteException.class, CarritoVacioException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        // Retorna la respuesta con el mensaje de la excepción y el código HTTP 400
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Maneja excepciones causadas por argumentos inválidos
    // 400 Bad Request - Cuando el cliente envía datos incorrectos o mal formados
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        // Retorna la respuesta con el mensaje de error y código HTTP 400
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}