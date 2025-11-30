package co.cue.inventario_service.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para el microservicio de Inventario.
 * Centraliza la captura y transformación de errores que ocurren durante la gestión
 * de productos, categorías y stock.
 * A diferencia de otros manejadores en el sistema, este incluye tratamiento específico
 * para violaciones de integridad de datos, lo cual es común en inventarios
 * (ej. intentar crear dos productos con el mismo nombre único o borrar una categoría en uso).
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de entidad no encontrada.
     * Captura errores lanzados cuando se intenta acceder a un Producto, Categoría o Kit
     * que no existe en la base de datos (o está marcado como inactivo).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de violación de integridad de datos.
     * Este método es crítico para el inventario. Se activa automáticamente cuando
     * Hibernate/JPA intenta ejecutar una operación SQL que rompe una restricción de la BD.
     * Casos comunes:
     * - Intentar registrar un producto con un nombre duplicado (Constraint Unique).
     * - Intentar eliminar una categoría que tiene productos asociados (Foreign Key Constraint).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Retornamos CONFLICT (409) para indicar que la solicitud es válida en sintaxis
        // pero incompatible con el estado actual de los datos.
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
