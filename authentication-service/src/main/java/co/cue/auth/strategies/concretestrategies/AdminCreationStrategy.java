package co.cue.auth.strategies.concretestrategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Admin;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.enums.TipoUsuario;
import co.cue.auth.strategies.IUsuarioCreationStrategy;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para la creación de usuarios de tipo ADMIN (Administradores).
 * Implementa la lógica necesaria para instanciar un nuevo administrador del sistema.
 * Nota de Seguridad:
 * Aunque técnicamente el proceso de creación es idéntico al de un Dueño (mismos campos),
 * esta clase instancia la entidad Admin, la cual nace con el rol ROLE_ADMIN.
 * El uso de esta estrategia debe estar estrictamente controlado por la capa de seguridad
 * (generalmente, solo otro Admin debería poder invocar un endpoint que use este tipo).
 */
@Component
public class AdminCreationStrategy implements IUsuarioCreationStrategy {

    /**
     * Crea e inicializa una nueva instancia de la entidad Admin.
     * Recibe los datos básicos del DTO y delega la construcción a la entidad Admin.
     * Importante:
     * No se requieren campos adicionales (como especialidad), pero la implicación
     * de seguridad es alta. El constructor de Admin asignará internamente los
     * permisos elevados necesarios para la gestión del sistema.
     */
    @Override
    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        // Instanciamos la clase Admin, lo que garantiza que el campo 'userRole'
        // se establecerá en ROLE_ADMIN automáticamente.
        return new Admin(
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getContrasenia(),
                dto.getDireccion(),
                dto.getTelefono()
        );
    }

    /**
     * Identifica el tipo de usuario que esta estrategia gestiona.
     * Retorna TipoUsuario.ADMIN, permitiendo que el UsuarioFactory sepa
     * qué lógica ejecutar cuando llega una solicitud de creación de administrador.
     */
    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.ADMIN;
    }
}
