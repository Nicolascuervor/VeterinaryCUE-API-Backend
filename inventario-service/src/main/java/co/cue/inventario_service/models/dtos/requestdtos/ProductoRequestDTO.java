package co.cue.inventario_service.models.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;


/**
 * DTO base abstracto para la creación y actualización de productos.
 * Esta clase define los atributos comunes que comparten todos los productos del inventario
 * (nombre, precio, stock, etc.), independientemente de su tipo específico.
 * Mecanismo de Polimorfismo (Jackson):
 * Se utilizan anotaciones especiales para permitir que el API reciba diferentes tipos de
 * productos en un mismo endpoint (o compartir estructura). El framework deserializa
 * automáticamente el JSON en la subclase correcta basándose en un campo discriminador.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Usamos un nombre lógico como identificador
        include = JsonTypeInfo.As.PROPERTY, // El identificador viaja como una propiedad más del JSON
        property = "tipoProducto" // Nombre del campo discriminador en el JSON (ej. "tipoProducto": "ALIMENTO")
)
@JsonSubTypes({
        // Mapeo entre el valor del discriminador y la clase Java concreta
        @JsonSubTypes.Type(value = AlimentoRequestDTO.class, name = "ALIMENTO"),
        @JsonSubTypes.Type(value = MedicinaRequestDTO.class, name = "MEDICINA"),
        @JsonSubTypes.Type(value = AccesorioRequestDTO.class, name = "ACCESORIO")
})
@Getter
@Setter
public abstract class ProductoRequestDTO {

    /**
     * Nombre comercial del producto.
     * Debe ser único en el inventario para evitar confusiones.
     */
    private String nombre;

    /**
     * Precio de venta al público unitario.
     */
    private Double precio;

    /**
     * Cantidad física disponible actualmente en el almacén.
     * Este valor se reduce con los pedidos y se aumenta con el reabastecimiento.
     */
    private Integer stockActual;

    /**
     * Nivel de stock de seguridad.
     * Si el stockActual cae por debajo de este número, el sistema podría
     * generar alertas de reabastecimiento (lógica futura).
     */
    private Integer stockMinimo;

    /**
     * Referencia física de dónde se encuentra el producto (ej. "Pasillo 3, Estante B").
     * Útil para la logística de bodega.
     */
    private String ubicacion;

    /**
     * Interruptor para habilitar o inhabilitar la venta del producto.
     * Permite tener productos en inventario (existencias > 0) pero no ofertarlos
     * en la tienda (ej. productos en cuarentena o temporada).
     */
    private boolean disponibleParaVenta;

    /**
     * Identificador de la categoría a la que pertenece el producto.
     * Se utiliza solo el ID para vincular la relación en la base de datos
     * sin necesidad de enviar el objeto categoría completo.
     */
    private Long categoriaId;
}