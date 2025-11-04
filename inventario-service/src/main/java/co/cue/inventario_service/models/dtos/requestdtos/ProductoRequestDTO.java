package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ProductoRequestDTO {

    private String nombre;
    private Double precio;
    private Integer stockActual;
    private Integer stockMinimo;
    private String ubicacion;
    private boolean disponibleParaVenta;


    private Long categoriaId;
}