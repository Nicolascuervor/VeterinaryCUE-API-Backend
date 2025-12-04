package co.cue.inventario_service.models.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
/**
 * DTO base para representar la información común de cualquier tipo de producto
 * dentro del inventario. Utiliza anotaciones de Jackson para manejar
 * la serialización/deserialización polimórfica según el tipo de producto.
 *
 * Los tipos soportados son:
 * - ALIMENTO → AlimentoResponseDTO
 * - MEDICINA → MedicinaResponseDTO
 * - ACCESORIO → AccesorioResponseDTO
 * - KIT → KitProductoResponseDTO
 */

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

    /** Identificador único del producto. */
    private Long id;

    /** Nombre del producto. */
    private String nombre;

    /** Descripción del producto para mostrar en el ecommerce. */
    private String descripcion;

    /** URL o ruta de la foto/imagen del producto. */
    private String foto;

    /** Precio actual del producto. */
    private Double precio;

    /** Cantidad disponible en inventario. */
    private Integer stockActual;

    /** Indica si el producto puede ser vendido. */
    private boolean disponibleParaVenta;

    /** Datos de la categoría a la que pertenece el producto. */
    private CategoriaResponseDTO categoria;
}
