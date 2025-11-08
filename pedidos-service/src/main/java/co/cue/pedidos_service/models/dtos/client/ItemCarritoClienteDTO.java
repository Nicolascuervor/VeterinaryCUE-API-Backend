package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemCarritoClienteDTO {
    private Long productoId;
    private Integer cantidad;
}