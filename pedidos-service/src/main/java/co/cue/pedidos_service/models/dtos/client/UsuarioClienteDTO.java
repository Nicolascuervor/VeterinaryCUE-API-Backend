package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UsuarioClienteDTO
 *
 * DTO (Data Transfer Object) utilizado para exponer la información básica de un usuario
 * hacia el cliente/consumidor del API. Su propósito es transportar únicamente los datos
 * necesarios del usuario sin incluir información sensible como contraseñas o roles internos.
 *
 * Propósito:
 * - Representar información básica del cliente para procesos como checkout,
 *   confirmaciones, visualización de perfil o compras.
 *
 * Uso típico:
 * - Asociar datos del usuario en pedidos.
 * - Mostrar información del cliente en flujos de compra.
 *
 * Atributos:
 *  nombre del usuario.
 * apellido del usuario.
 *  correo electrónico principal del usuario.
 */
@Getter
@Setter
@NoArgsConstructor
public class UsuarioClienteDTO {
    /**
     * Nombre del cliente.
     */
    private String nombre;
    /**
     * Apellido del cliente.
     */
    private String apellido;
    /**
     * Correo electrónico del cliente, utilizado para contacto y notificaciones.
     */
    private String correo;
}
