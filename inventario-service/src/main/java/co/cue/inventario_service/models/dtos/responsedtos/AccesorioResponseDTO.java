package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la respuesta de productos tipo ACCESORIO.
 * Esta clase se utiliza para enviar la información detallada de un accesorio al cliente
 * (Frontend) cuando se consultan productos individuales o el catálogo general.
 * Extiende de ProductoResponseDTO para heredar la estructura base (id, nombre, precio, stock)
 * y añade los atributos específicos que caracterizan a un accesorio. Esto permite
 * que la interfaz de usuario muestre detalles relevantes como el material y el tamaño
 * sin mezclar campos de otros tipos de productos.
 */
@Getter
@Setter
public class AccesorioResponseDTO extends ProductoResponseDTO {

    // --- Atributos Específicos ---

    /**
     * Material principal de fabricación del accesorio.
     * Dato informativo para el cliente (ej. "Cuero", "Nailon", "Plástico").
     */
    private String material;

    /**
     * Tamaño, talla o dimensiones del accesorio.
     * Información crítica para que el cliente sepa si el producto es adecuado
     * para su mascota (ej. "S", "M", "L" o "120cm").
     */
    private String tamanio;
}
