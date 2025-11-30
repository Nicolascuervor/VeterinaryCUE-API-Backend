package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la respuesta de productos tipo MEDICINA.
 * Esta clase se utiliza para enviar la información detallada de un medicamento al cliente
 * (Frontend) cuando se consultan productos individuales o el catálogo general.
 * Extiende de ProductoResponseDTO para heredar la estructura base (id, nombre, precio, stock)
 * y añade los atributos farmacológicos específicos. Esto garantiza que el consumidor de la API
 * reciba un contrato de datos consistente, enriquecido con la información sanitaria necesaria.
 */
@Getter
@Setter
public class MedicinaResponseDTO extends ProductoResponseDTO {

    // --- Atributos Específicos de Farmacia ---

    /**
     * Detalle de los componentes activos y excipientes del medicamento.
     * Esta información es vital para que el veterinario o el dueño puedan verificar
     * la idoneidad del fármaco y prevenir reacciones alérgicas en la mascota.
     * Se expone en la ficha técnica del producto en la tienda.
     */
    private String composicion;

    /**
     * Instrucciones o pautas generales de administración sugeridas.
     * Proporciona una guía rápida de uso (ej. "10ml por cada 5kg de peso").
     * Aunque la prescripción final depende del criterio veterinario, este campo
     * ofrece contexto valioso sobre la posología del producto.
     */
    private String dosisRecomendada;
}
