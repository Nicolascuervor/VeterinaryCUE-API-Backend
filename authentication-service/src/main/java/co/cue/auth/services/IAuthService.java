package co.cue.auth.services;
import co.cue.auth.models.dtos.ActualizarUsuarioDTO;
import co.cue.auth.models.dtos.AuthResponseDTO;
import co.cue.auth.models.dtos.LoginRequestDTO;
import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;

import java.util.List;

/**
 * Interfaz que define el contrato de servicios para la gestión de autenticación y usuarios.
 * Esta interfaz desacopla la definición de las operaciones de su implementación concreta,
 * facilitando el testing (mocks), la mantenibilidad y la aplicación de patrones como Proxy
 * (utilizado para las transacciones y la seguridad).
 */
public interface IAuthService {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Este método coordina la validación de reglas de negocio (unicidad de correo),
     * la encriptación de contraseñas y la persistencia inicial de la entidad.
     */
    Usuario registrarUsuario(RegistroUsuarioDTO dto);

    /**
     * Recupera el listado de todos los usuarios que se encuentran activos.
     * Utilizado principalmente por administradores para monitorear la base de usuarios
     * operativa del sistema.
     */
    List<Usuario> obtenerUsuariosActivos();

    /**
     * Busca un usuario activo por su dirección de correo electrónico.
     * Es fundamental para procesos que requieren identificar al usuario por su alias
     * (como la recuperación de contraseñas o validaciones de unicidad).
     */
    Usuario obtenerUsuarioPorCorreo(String correo);

    /**
     * Busca un usuario activo por su identificador único (Primary Key).
     *
     * Operación estándar para cargar perfiles de usuario.
     */
    Usuario obtenerUsuarioPorId(Long id);

    /**
     * Actualiza los datos modificables de un usuario existente.
     *
     * Aplica los cambios recibidos en el DTO sobre la entidad persistida.
     * Maneja la lógica para actualizar campos específicos según el tipo de usuario
     * (ej. especialidad para veterinarios).
     */
    Usuario actualizarUsuario(Long id, ActualizarUsuarioDTO dto);

    /**
     * Desactiva la cuenta de un usuario (Soft Delete).
     *
     * Cambia el estado del usuario a inactivo, impidiendo futuros inicios de sesión
     * sin eliminar sus datos históricos.
     */
    void desactivarUsuario(Long id);

    /**
     * Procesa la autenticación de un usuario mediante credenciales.
     * Verifica el correo y contraseña contra la base de datos. Si son válidos,
     * genera y retorna un token de acceso (JWT).
     */
    AuthResponseDTO login(LoginRequestDTO dto);
}
