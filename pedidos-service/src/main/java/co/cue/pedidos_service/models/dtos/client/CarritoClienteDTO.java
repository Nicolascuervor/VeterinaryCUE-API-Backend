package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
/**
 * CarritoClienteDTO
 *
 * Representa la estructura de datos que contiene los ítems del carrito de un cliente.
 * Esta clase funciona como un DTO (Data Transfer Object) para transferir información
 * relacionada con el carrito en las operaciones del sistema.
 *
 * Principales características:
 * - Utiliza un conjunto (Set) de ItemCarritoClienteDTO para evitar duplicados.
 * - Se usa en controladores y servicios para transportar la información del carrito.
 *
 * Uso típico:
 * - Enviar o recibir datos del carrito del cliente en solicitudes HTTP.
 * - Agrupar todos los productos seleccionados por el cliente.
 *
 * Atributos:
 * conjunto de elementos que componen el carrito del cliente.
 */
@Getter
@Setter
@NoArgsConstructor
public class CarritoClienteDTO {
    /**
     * Conjunto de ítems del carrito del cliente.
     * Cada elemento contiene información sobre el producto y la cantidad.
     */
    private Set<ItemCarritoClienteDTO> items;
}