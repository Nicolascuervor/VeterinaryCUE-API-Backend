package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de respuesta que representa una categoría dentro del sistema de inventario.
 * Contiene información básica como el identificador, nombre y descripción.
 */
@Getter
@Setter
public class CategoriaResponseDTO {

    // Identificador único de la categoría
    private Long id;

    // Nombre de la categoría (ej: Alimentos, Medicinas, Accesorios)
    private String nombre;

    // Descripción opcional de la categoría
    private String descripcion;
}
