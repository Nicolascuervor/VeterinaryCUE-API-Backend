package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlimentoRequestDTO extends ProductoRequestDTO {

    // Tipo de mascota al que va dirigido el alimento (perro, gato, etc.)
    private String tipoMascota;

    // Peso del alimento en kilogramos
    private Double pesoEnKg;
}
