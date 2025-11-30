package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la respuesta de información de Categorías.
 * Esta clase se utiliza para enviar los datos de las categorías al cliente (Frontend).
 * Su principal uso es poblar componentes de interfaz de usuario como:
 * - Menús de navegación principal.
 * - Listas desplegables (selects) en formularios de creación de productos.
 * - Filtros de búsqueda en el catálogo.
 * Al ser un DTO de respuesta, expone el ID de base de datos, necesario para que el
 * cliente pueda realizar peticiones posteriores (como filtrar productos por categoría).
 */
@Getter
@Setter
public class CategoriaResponseDTO {

    /**
     * Identificador único de la categoría.
     * Dato técnico necesario para vincular productos o realizar consultas específicas
     * desde el frontend.
     */
    private Long id;

    /**
     * Nombre visible de la categoría.
     * Etiqueta principal que se mostrará al usuario en la interfaz.
     */
    private String nombre;

    /**
     * Descripción corta de la categoría.
     * Puede utilizarse en tooltips o secciones de ayuda visual para orientar al usuario
     * sobre qué tipo de productos encontrará en esta sección.
     */
    private String descripcion;
}
