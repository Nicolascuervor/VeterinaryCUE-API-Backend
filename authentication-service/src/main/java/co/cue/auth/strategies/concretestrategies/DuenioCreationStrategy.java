package co.cue.auth.strategies.concretestrategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Duenio;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.enums.TipoUsuario;
import co.cue.auth.strategies.IUsuarioCreationStrategy;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para la creación de usuarios de tipo DUEÑO (Clientes).
 * Implementa la interfaz IUsuarioCreationStrategy para encapsular la lógica
 * de instanciación de los clientes de la veterinaria.
 * A diferencia de la estrategia de Veterinarios, esta implementación es directa
 * y no requiere validaciones complejas de atributos adicionales, ya que la entidad
 * Duenio comparte la misma estructura básica que la entidad Usuario padre.
 */
@Component
public class DuenioCreationStrategy implements IUsuarioCreationStrategy {

    /**
     * Crea una nueva instancia de la entidad Duenio.
     * Recibe el DTO genérico de registro y extrae los campos comunes (nombre, correo, etc.)
     * para invocar el constructor específico de la clase Duenio.
     * El constructor de la entidad se encargará internamente de asignar el rol de seguridad
     * ROLE_DUENIO, garantizando que el usuario nazca con los permisos correctos.
     */
    @Override
    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        // --- Construcción de la Entidad ---
        // Instanciación directa. No hay reglas de negocio complejas para validar aquí
        // más allá de las validaciones de formato (Email, NotBlank) que ya pasaron en el DTO.
        return new Duenio(
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getContrasenia(),
                dto.getDireccion(),
                dto.getTelefono()
        );
    }

    /**
     * Identifica el tipo de usuario que esta estrategia sabe construir.
     * Retorna TipoUsuario DUENIO para que el UsuarioFactory pueda mapear esta clase
     * como la encargada de procesar las solicitudes de registro de clientes.
     */
    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.DUENIO;
    }
}