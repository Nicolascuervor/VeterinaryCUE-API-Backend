package co.cue.inventario_service.models.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

// Configuración para permitir polimorfismo en JSON.
// El campo "tipoProducto" indicará qué subclase se debe deserializar.
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Usa el nombre del tipo para distinguir clases
        include = JsonTypeInfo.As.PROPERTY,   // El nombre del tipo viene como propiedad en el JSON
        property = "tipoProducto"    // Propiedad que debe enviar el cliente

)

// Declaración de subtipos permitidos para ProductoRequestDTO.
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlimentoRequestDTO.class, name = "ALIMENTO"),
        @JsonSubTypes.Type(value = MedicinaRequestDTO.class, name = "MEDICINA"),
        @JsonSubTypes.Type(value = AccesorioRequestDTO.class, name = "ACCESORIO")
})
@Getter
@Setter
public abstract class ProductoRequestDTO {

    // Nombre del producto
    private String nombre;

    // Precio del producto
    private Double precio;

    // Cantidad disponible actualmente en inventario
    private Integer stockActual;

    // Cantidad mínima antes de generar alerta de stock bajo
    private Integer stockMinimo;

    // Ubicación física del producto en el inventario
    private String ubicacion;

    // Indica si el producto está disponible para venta
    private boolean disponibleParaVenta;

    // ID de la categoría asociada al producto
    private Long categoriaId;
}