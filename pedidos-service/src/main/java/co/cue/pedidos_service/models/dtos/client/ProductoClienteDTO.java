package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * ProductoClienteDTO
 *
 * DTO (Data Transfer Object) utilizado para exponer la información de un producto
 * hacia el cliente/consumidor del API. Este objeto garantiza que únicamente se
 * entregue la información necesaria, evitando enviar datos sensibles o irrelevantes.
 *
 * Propósito:
 * - Representar un producto en las operaciones de consulta del carrito o catálogo.
 * - Facilitar la transferencia de datos entre la capa de negocio y la capa de presentación.
 *
 * Uso típico:
 * - Mostrar productos disponibles para compra.
 * - Exponer información de productos dentro del carrito o listas de productos.
 *
 * Atributos:
 *  identificador único del producto.
 *  nombre del producto tal como se muestra al cliente.
 * precio actual del producto.
 * cantidad disponible en inventario.
 * indica si el producto se encuentra habilitado para compra.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductoClienteDTO {
    /**
     * Identificador único del producto.
     */
    private Long id;
    /**
     * Nombre del producto presentado al cliente.
     */
    private String nombre;
    /**
     * Precio de venta del producto.
     */
    private BigDecimal precio;
    /**
     * Cantidad actual disponible en inventario.
     */
    private Integer stockActual;
    /**
     * Indica si el producto está activo y disponible para ser mostrado o vendido.
     */
    private boolean disponibleParaVenta;

}