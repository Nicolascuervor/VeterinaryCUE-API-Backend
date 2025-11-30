package co.cue.auth.models.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la actualización de perfil de usuario.
 * Este DTO encapsula exclusivamente los campos que un usuario tiene permitido modificar
 * después de su registro inicial.
 * Patrón de Diseño (DTO):
 * Utilizamos una clase específica para la actualización en lugar de reutilizar la entidad Usuario
 * o él RegistroUsuarioDTO por razones de seguridad y diseño:
 * Seguridad: Evitamos exponer campos sensibles o inmutables como id, correo,
 * contrasenia o rol en la operación de actualización. Esto previene ataques de
 * asignación masiva (Mass Assignment).
 * Desacoplamiento: La vista (Frontend) puede enviar solo estos datos sin conocer la estructura
 * interna completa de la base de datos.
 */
@Getter
@Setter
public class ActualizarUsuarioDTO {

    //Información Personal Modificable
    // Estos campos representan la información básica de contacto e identidad
    // que no afecta la seguridad de la cuenta.
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String foto;


    // Campos Específicos de Roles
    // Este campo es opcional y solo relevante si el usuario es un VETERINARIO.
    // La lógica de negocio en el servicio se encargará de ignorarlo si el usuario es un Dueño o Admin.
    private String especialidad;
}
