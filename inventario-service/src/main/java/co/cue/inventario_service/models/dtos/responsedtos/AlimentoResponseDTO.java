package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la respuesta de productos tipo ALIMENTO.
 * Esta clase se utiliza para enviar la información detallada de un alimento al cliente
 * (Frontend) cuando se consultan productos individuales o el catálogo general.
 * Extiende de ProductoResponseDTO para heredar la estructura base (id, nombre, precio, stock)
 * y añade los atributos específicos que caracterizan a un alimento. Esto permite
 * que la interfaz de usuario muestre detalles relevantes como el peso y la especie objetivo
 * sin mezclar campos de otros tipos de productos.
 */
@Getter
@Setter
public class AlimentoResponseDTO extends ProductoResponseDTO {

    // --- Atributos Específicos

    /**
     * Especie objetivo para la cual está formulado el alimento (ej. "Perro", "Gato").
     *
     * Este dato es esencial para la experiencia de usuario, permitiendo mostrar etiquetas
     * claras o iconos en la interfaz de la tienda.
     */
    private String tipoMascota;

    /**
     * Peso neto del producto expresado en kilogramos.
     *
     * Información crítica para el cliente para evaluar la relación cantidad-precio
     * y para que el sistema calcule posibles costos de envío en el carrito de compras.
     */
    private Double pesoEnKg;
}
