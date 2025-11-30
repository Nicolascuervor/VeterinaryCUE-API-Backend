package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;
/**
 * DTO de respuesta para productos de tipo Medicina.
 * Extiende de ProductoResponseDTO e incluye campos específicos
 * relacionados con medicamentos dentro del inventario.
 */
@Getter
@Setter
public class MedicinaResponseDTO extends ProductoResponseDTO {

    /** Composición química o fórmula del medicamento. */
    private String composicion;

    /** Dosis recomendada para el uso del producto. */
    private String dosisRecomendada;
}
