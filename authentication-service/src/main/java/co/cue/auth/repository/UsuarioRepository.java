package co.cue.auth.repository;

import co.cue.auth.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repositorio de acceso a datos para la entidad Usuario.
 *
 * Extiende JpaRepository para proporcionar operaciones CRUD estándar y paginación
 * sobre la tabla 'usuarios'.
 *
 * Además de las consultas básicas, define métodos derivados (Query Methods)
 * para manejar la lógica de usuarios activos vs inactivos (Soft Delete).
 * Spring Data JPA genera automáticamente la implementación SQL de estas firmas.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo electrónico, independientemente de su estado.
     *
     * Este método ignora si el usuario está activo o inactivo.
     * Se utiliza para validaciones de integridad (ej. evitar duplicados al registrar)
     * o para procesos de auditoría interna.
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Verifica eficientemente si un correo ya está registrado en el sistema.
     *
     * Retorna true/false sin cargar toda la entidad en memoria, lo cual es ideal
     * para validaciones rápidas en el proceso de registro.
     */
    boolean existsByCorreo(String correo);

    /**
     * Busca un usuario por ID, asegurando que esté activo.
     *
     * Este es el método principal para recuperar perfiles en la lógica de negocio.
     * Implementa el patrón de Soft Delete: si el usuario existe físicamente pero
     * tiene el campo activo=false, este método retornará vacío (Empty),
     * simulando que el usuario no existe para la aplicación.
     */
    Optional<Usuario> findByIdAndActivoTrue(Long id);

    /**
     * Busca un usuario por correo para autenticación, asegurando que esté activo.
     *
     * Utilizado por el UserDetailsService durante el login.
     * Garantiza que un usuario desactivado (baneado o eliminado lógicamente)
     * no pueda iniciar sesión, incluso si sus credenciales son correctas.
     */
    Optional<Usuario> findByCorreoAndActivoTrue(String correo);

    /**
     * Lista todos los usuarios que están actualmente habilitados en el sistema.
     */
    List<Usuario> findAllByActivoTrue();
}
