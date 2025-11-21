package co.cue.carrito_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemRequestDTO {
    private Long productoId;
    private Integer cantidad;
}