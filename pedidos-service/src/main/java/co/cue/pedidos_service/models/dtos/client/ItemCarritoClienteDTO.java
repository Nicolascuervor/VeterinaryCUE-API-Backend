package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ItemCarritoClienteDTO
 *
 * DTO (Data Transfer Object) que representa un ítem dentro del carrito de un cliente.
 * Cada ítem contiene la referencia a un producto y la cantidad seleccionada.
 *
 * Propósito:
 * - Facilitar la transferencia de datos entre las capas del sistema.
 * - Estandarizar la información que representa un ítem del carrito.
 *
 * Uso típico:
 * - Enviar y recibir la información de cada producto dentro del carrito.
 * - Formar parte de estructuras más amplias como CarritoClienteDTO.
 *
 * Atributos:
 *  identificador único del producto incluido en el carrito.
 * cantidad del producto seleccionada por el cliente.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemCarritoClienteDTO {
    /**
     * Identificador del producto agregado al carrito.
     */
    private Long productoId;
    /**
     * Cantidad del producto que el cliente desea adquirir.
     */
    private Integer cantidad;
}