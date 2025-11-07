package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class ProductoClienteDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stockActual;
    private boolean disponibleParaVenta;
}