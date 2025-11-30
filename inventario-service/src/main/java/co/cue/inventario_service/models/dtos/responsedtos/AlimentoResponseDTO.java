package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;


/**
 * DTO de respuesta para productos de tipo Alimento.
 * Extiende de ProductoResponseDTO para incluir los datos comunes del producto.
 */
@Getter
@Setter
public class AlimentoResponseDTO extends ProductoResponseDTO {

    // Tipo de mascota al que va dirigido el alimento (ej: perro, gato)
    private String tipoMascota;

    // Peso del alimento en kilogramos
    private Double pesoEnKg;
}
