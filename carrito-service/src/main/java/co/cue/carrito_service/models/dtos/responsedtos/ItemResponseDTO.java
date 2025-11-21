package co.cue.carrito_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemResponseDTO {
    private Long productoId;
    private Integer cantidad;
}
