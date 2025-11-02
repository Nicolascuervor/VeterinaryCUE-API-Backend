package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
