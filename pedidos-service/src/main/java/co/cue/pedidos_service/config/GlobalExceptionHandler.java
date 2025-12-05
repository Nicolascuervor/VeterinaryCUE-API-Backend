package co.cue.pedidos_service.config;

import co.cue.pedidos_service.exceptions.CarritoVacioException;
import co.cue.pedidos_service.exceptions.StockInsuficienteException;
import co.cue.pedidos_service.pasarela.exceptions.PasarelaPagoException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Indica que esta clase manejará excepciones globales en toda la aplicación
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Maneja excepciones relacionadas con errores de lógica del lado del cliente
    // 400 Bad Request - Cuando no hay suficiente stock o el carrito está vacío
    @ExceptionHandler({StockInsuficienteException.class, CarritoVacioException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        log.error("Error de validación: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Maneja excepciones causadas por argumentos inválidos
    // 400 Bad Request - Cuando el cliente envía datos incorrectos o mal formados
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Argumento inválido: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Maneja excepciones relacionadas con la pasarela de pagos (Stripe)
    // 500 Internal Server Error - Cuando hay problemas con Stripe
    @ExceptionHandler(PasarelaPagoException.class)
    public ResponseEntity<Map<String, Object>> handlePasarelaPagoException(PasarelaPagoException ex) {
        log.error("Error en la pasarela de pagos: {}", ex.getMessage(), ex);
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Payment Gateway Error");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        // Si la causa es una excepción de Stripe, proporcionar más detalles
        if (ex.getCause() != null) {
            String causa = ex.getCause().getMessage();
            if (causa != null && causa.contains("Invalid API Key")) {
                error.put("message", "Error de configuración: La clave API de Stripe es inválida. Verifique que STRIPE_SECRET_KEY sea una clave secreta (sk_test_...) y no una clave pública (pk_test_...).");
            }
        }
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Maneja excepciones cuando una entidad no es encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entidad no encontrada: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Maneja cualquier otra excepción no capturada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "Ocurrió un error inesperado al procesar la solicitud");
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}