package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de respuesta para productos de tipo Accesorio.
 * Extiende de ProductoResponseDTO para heredar los datos básicos comunes.
 */
@Getter
@Setter
public class AccesorioResponseDTO extends ProductoResponseDTO {

    // Material del accesorio (ej: plástico, tela, metal)
    private String material;

    // Tamaño del accesorio (ej: S, M, L)
    private String tamanio;
}
