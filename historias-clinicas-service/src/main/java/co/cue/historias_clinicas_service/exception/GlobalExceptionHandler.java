package co.cue.historias_clinicas_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para el servicio de historiales clínicos.
 * Captura excepciones y devuelve respuestas HTTP apropiadas con mensajes descriptivos.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja errores de deserialización JSON (formato incorrecto, campos faltantes, etc.)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Error al deserializar el JSON: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error en el formato de la petición");
        error.put("mensaje", "El JSON enviado no es válido. Verifica el formato de los datos, especialmente las fechas (formato: YYYY-MM-DD)");
        error.put("detalle", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Maneja errores de validación de argumentos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Error de validación: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error de validación");
        error.put("mensaje", "Los datos enviados no cumplen con las validaciones requeridas");
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Maneja errores de argumentos ilegales (validaciones de negocio)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Error de validación de negocio: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error de validación");
        error.put("mensaje", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Maneja todas las demás excepciones no capturadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        log.error("Error inesperado: {}", e.getMessage(), e);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("mensaje", "Ocurrió un error al procesar la petición");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

