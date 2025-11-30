package co.cue.auth.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para el microservicio de Autenticación.
 * Esta clase intercepta las excepciones lanzadas por cualquier controlador (Controller)
 * dentro de este microservicio y las transforma en respuestas HTTP estructuradas y amigables.
 * Utiliza la anotación ControllerAdvice para aplicar esta lógica de forma transversal,
 * evitando tener bloques try-catch repetitivos en cada endpoint de la API.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    /**
     * Maneja excepciones de tipo EntityNotFoundException.
     * Se activa comúnmente cuando se intenta buscar un usuario por ID o correo y este no existe
     * en la base de datos (lanzado por los repositorios o servicios JPA).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de tipo IllegalArgumentException.
     * Se utiliza para capturar errores de lógica de negocio o validaciones manuales,
     * como intentar registrar un usuario con un correo que ya existe o datos incoherentes.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de autenticación fallida AuthenticationException.
     * Este método captura los errores lanzados por Spring Security durante el proceso de login,
     * como cuando el AuthenticationManager rechaza las credenciales (usuario/contraseña incorrectos).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException() {
        // Devolvemos un mensaje genérico "Credenciales inválidas" en lugar del mensaje
        // detallado de la excepción para no dar pistas a posibles atacantes sobre si el usuario existe o no.
        return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja excepciones de validación de argumentos MethodArgumentNotValidException.
     * Se activa automáticamente cuando fallan las validaciones de Spring (@Valid, @NotNull, @Email, etc.)
     * en los DTO de entrada de los controladores (ej. al registrar un usuario).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Transformación de Errores
        // Convertimos la lista compleja de 'FieldErrors' de Spring en un mapa simple clave-valor
        // para que el frontend pueda mostrar fácilmente qué campo falló y por qué.
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<String> handleFileStorageException(FileStorageException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
