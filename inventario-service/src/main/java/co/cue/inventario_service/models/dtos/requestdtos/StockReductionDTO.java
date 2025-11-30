package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor  // Genera constructor con todos los atributos
@NoArgsConstructor  // Genera constructor vacío
public class StockReductionDTO {

    // ID del producto al que se le desea descontar stock
    private Long productoId;

    // Cantidad que se debe restar del inventario
    private Integer cantidad;
}
