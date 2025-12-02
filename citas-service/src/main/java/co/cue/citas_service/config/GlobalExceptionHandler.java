package co.cue.citas_service.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleBusinessRule(IllegalStateException ex) {
        // Retorna 400 en lugar de 500 para reglas de negocio rotas
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Manejar errores de validación de negocio (ej: Agenda ocupada, Veterinario no trabaja)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleBusinessConflict(IllegalStateException ex) {
        // Devolvemos 400 BAD REQUEST o 409 CONFLICT para que el front sepa que es un error lógico
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Manejar argumentos inválidos (ej: Fechas nulas)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
