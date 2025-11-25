package co.cue.auth.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Objeto de Transferencia de Datos (DTO) para la respuesta de autenticación exitosa.
 * Este objeto se retorna al cliente (Frontend) inmediatamente después de que un usuario
 * realiza un inicio de sesión (Login) correcto. Su única función es transportar el
 * Token de Acceso (JWT) generado.
 * El cliente deberá almacenar este token (ej. en LocalStorage o Cookies seguras) y
 * enviarlo en la cabecera Authorization: Bearer token en todas las
 * solicitudes posteriores a rutas protegidas.
 */
@Getter
@AllArgsConstructor
public class AuthResponseDTO {

    /**
     * El JSON Web Token (JWT) firmado criptográficamente.
     * Contiene la identidad del usuario (Subject), sus roles (Authorities) y la fecha de expiración.
     * Es la credencial que la API Gateway validará para permitir el acceso.
     */
    private String token;
}
