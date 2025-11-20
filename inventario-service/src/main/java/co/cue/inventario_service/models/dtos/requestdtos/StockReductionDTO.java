package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockReductionDTO {
    private Long productoId;
    private Integer cantidad;
}
