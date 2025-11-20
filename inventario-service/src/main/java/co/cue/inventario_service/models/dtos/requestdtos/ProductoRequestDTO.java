package co.cue.inventario_service.models.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoProducto"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = AlimentoRequestDTO.class, name = "ALIMENTO"),
        @JsonSubTypes.Type(value = MedicinaRequestDTO.class, name = "MEDICINA"),
        @JsonSubTypes.Type(value = AccesorioRequestDTO.class, name = "ACCESORIO")
})
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