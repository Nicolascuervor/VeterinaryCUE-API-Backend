package co.cue.pedidos_service.config;

import co.cue.pedidos_service.exceptions.CarritoVacioException;
import co.cue.pedidos_service.exceptions.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request - Errores de lógica de negocio (cliente)
    @ExceptionHandler({StockInsuficienteException.class, CarritoVacioException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 400 Bad Request - Errores de sintaxis (cliente)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}