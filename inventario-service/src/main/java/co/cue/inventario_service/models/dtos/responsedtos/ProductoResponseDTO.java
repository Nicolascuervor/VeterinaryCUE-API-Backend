package co.cue.inventario_service.models.dtos.responsedtos;

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
        @JsonSubTypes.Type(value = AlimentoResponseDTO.class, name = "ALIMENTO"),
        @JsonSubTypes.Type(value = MedicinaResponseDTO.class, name = "MEDICINA"),
        @JsonSubTypes.Type(value = AccesorioResponseDTO.class, name = "ACCESORIO"),
        @JsonSubTypes.Type(value = KitProductoResponseDTO.class, name = "KIT")
})
@Getter
@Setter
public abstract class ProductoResponseDTO {

    // (Mentor): Campos comunes que todos los JSON de respuesta tendrán.
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stockActual;
    private boolean disponibleParaVenta;

    // (Mentor): Incluimos el DTO de Categoría que ya creamos.
    private CategoriaResponseDTO categoria;
}
