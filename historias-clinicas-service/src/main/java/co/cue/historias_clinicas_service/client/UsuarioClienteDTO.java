package co.cue.historias_clinicas_service.client;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para recibir datos del usuario
 * obtenidos desde el microservicio de autenticación.
 */
@Data
@NoArgsConstructor
public class UsuarioClienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
}

