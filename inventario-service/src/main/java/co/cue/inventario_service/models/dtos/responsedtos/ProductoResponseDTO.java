package co.cue.inventario_service.models.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;


/**
 * DTO base abstracto para la respuesta de información de productos.
 * Esta clase define la estructura común de datos que comparten todos los ítems del inventario
 * al ser enviados al cliente (ID, nombre, precio, stock, categoría).
 * Mecanismo de Polimorfismo en Serialización (Jackson):
 * Al igual que en los DTOs de entrada, utilizamos anotaciones para habilitar el polimorfismo.
 * Cuando el servidor responde con una lista de este tipo, Jackson inyecta automáticamente
 * un campo extra llamado "tipoProducto" en el JSON.
 * Esto permite al cliente (Frontend) distinguir si un ítem específico de la lista es un
 * ALIMENTO, una MEDICINA o un KIT, y renderizar la vista correspondiente sin ambigüedades.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoProducto" // El campo discriminador que se añadirá al JSON de respuesta
)
@JsonSubTypes({
        // Registro de las subclases conocidas para la serialización polimórfica
        @JsonSubTypes.Type(value = AlimentoResponseDTO.class, name = "ALIMENTO"),
        @JsonSubTypes.Type(value = MedicinaResponseDTO.class, name = "MEDICINA"),
        @JsonSubTypes.Type(value = AccesorioResponseDTO.class, name = "ACCESORIO"),
        @JsonSubTypes.Type(value = KitProductoResponseDTO.class, name = "KIT")
})
@Getter
@Setter
public abstract class ProductoResponseDTO {

    /**
     * Identificador único del producto en la base de datos.
     * Dato técnico necesario para operaciones posteriores (añadir al carrito, ver detalle).
     */
    private Long id;

    /**
     * Nombre comercial del producto.
     * Se muestra como título principal en las tarjetas del catálogo.
     */
    private String nombre;

    /**
     * Precio de venta unitario vigente.
     */
    private Double precio;

    /**
     * Cantidad de unidades disponibles para la venta inmediata.
     * El frontend puede usar este dato para mostrar etiquetas de "Agotado" o "Pocas unidades".
     */
    private Integer stockActual;

    /**
     * Indicador de disponibilidad comercial.
     * Si es false, el producto existe pero no debería permitirse su compra (aunque quizás sí su visualización como "No disponible").
     */
    private boolean disponibleParaVenta;

    /**
     * Información resumida de la categoría a la que pertenece el producto.
     * Se incluye el objeto DTO completo (id, nombre) para facilitar la navegación o agrupación en la UI.
     */
    private CategoriaResponseDTO categoria;
}
