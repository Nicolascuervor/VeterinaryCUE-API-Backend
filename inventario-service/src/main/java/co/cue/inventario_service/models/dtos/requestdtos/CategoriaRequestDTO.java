package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;


/**
 * Objeto de Transferencia de Datos (DTO) para la gestión de Categorías.
 * Este objeto encapsula la información necesaria para crear o actualizar
 * una categoría de productos en el sistema.
 * Las categorías actúan como agrupadores lógicos (ej. "Alimentos", "Juguetes", "Medicamentos")
 * y son esenciales para la navegación y el filtrado en el frontend de la tienda.
 */
@Getter
@Setter
public class CategoriaRequestDTO {

    /**
     * Nombre único de la categoría (ej. "Perros - Alimento Seco").
     * Este campo debe ser único en el sistema para evitar ambigüedades.
     * Se utiliza como etiqueta principal en la interfaz de usuario y los filtros de búsqueda.
     */
    private String nombre;

    /**
     * Descripción detallada del propósito de la categoría.
     * Proporciona contexto adicional para los administradores o para mostrar
     * en tooltips/ayudas en la interfaz de usuario, explicando qué tipo de
     * productos deben agruparse aquí.
     */
    private String descripcion;
}
