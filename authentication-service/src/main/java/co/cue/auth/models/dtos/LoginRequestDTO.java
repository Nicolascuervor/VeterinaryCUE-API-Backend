package co.cue.auth.models.dtos;

import lombok.Getter;
import lombok.Setter;


/**
 * Objeto de Transferencia de Datos (DTO) para la solicitud de inicio de sesión.
 * Este DTO encapsula las credenciales crudas (correo y contraseña) que el cliente
 * envía al servidor para intentar autenticarse.
 * Principio de Diseño: Separamos este objeto de la entidad Usuario y del
 * RegistroUsuarioDTO para mantener un contrato de entrada estricto y mínimo:
 * el login solo requiere identificar quién eres (correo) y probarlo (contraseña).
 */
@Getter
@Setter
public class LoginRequestDTO {

    // Credenciales de Acceso

    /**
     * El correo electrónico registrado del usuario.
     * Actúa como el identificador principal (username) para la autenticación.
     */
    private String correo;

    /**
     * La contraseña en texto plano proporcionada por el usuario.
     */
    private String contrasenia;
}
