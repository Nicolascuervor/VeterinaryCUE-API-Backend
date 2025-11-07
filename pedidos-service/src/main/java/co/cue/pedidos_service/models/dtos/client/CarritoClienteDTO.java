package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CarritoClienteDTO {
    private Set<ItemCarritoClienteDTO> items;
}