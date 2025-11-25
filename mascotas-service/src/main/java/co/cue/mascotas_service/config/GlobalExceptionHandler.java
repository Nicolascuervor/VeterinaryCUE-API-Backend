package co.cue.mascotas_service.config;

import co.cue.mascotas_service.exceptions.MascotaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
// Maneja de forma centralizada las excepciones de toda la aplicación.
public class GlobalExceptionHandler {

    @ExceptionHandler(MascotaNotFoundException.class)
    // Captura la excepción cuando una mascota no es encontrada y devuelve un mensaje apropiado.
    public ResponseEntity<String> handleMascotaNotFound(MascotaNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
