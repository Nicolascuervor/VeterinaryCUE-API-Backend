package co.cue.auth.strategies.concretestrategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.entities.Veterinario;
import co.cue.auth.models.enums.TipoUsuario;
import co.cue.auth.strategies.IUsuarioCreationStrategy;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para la creación de usuarios de tipo VETERINARIO.
 * Esta clase implementa la lógica específica requerida para instanciar un profesional médico.
 * Su responsabilidad principal es asegurar que se cumplan las reglas de negocio
 * exclusivas para los veterinarios (como la obligatoriedad de la especialidad)
 * antes de construir el objeto.
 * Al ser un componente de Spring (@Component), se inyectará automáticamente
 * en la lista de estrategias del UsuarioFactory.
 */
@Component
public class VeterinarioCreationStrategy implements IUsuarioCreationStrategy {

    /**
     * Crea e inicializa una instancia de la entidad Veterinario.
     * Este método aplica una validación de negocio crucial: verifica que el DTO
     * contenga una especialidad válida. Si no es así, rechaza la creación lanzando
     * una excepción, protegiendo la integridad de los datos (no pueden existir
     * veterinarios sin especialidad en el sistema).
     */
    @Override
    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        // Validación de Reglas de Negocio Específicas
        // Aunque el DTO permite que 'especialidad' sea nulo (porque es opcional para Dueños),
        // en este contexto específico es un dato obligatorio.
        if (dto.getEspecialidad() == null || dto.getEspecialidad().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es requerida para el tipo VETERINARIO");
        }

        // Construcción de la Entidad
        // Utilizamos el constructor específico de Veterinario que se encarga de
        // asignar el rol ROLE_VETERINARIO internamente.
        return new Veterinario(
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getContrasenia(), // Ya viene hasheada desde el servicio
                dto.getDireccion(),
                dto.getTelefono(),
                dto.getEspecialidad()
        );
    }

    /**
     * Identifica a esta estrategia como la encargada de manejar el tipo VETERINARIO.
     * Utilizado por el UsuarioFactory para asociar el enum TipoUsuario.VETERINARIO
     * con esta implementación.
     */
    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.VETERINARIO;
    }
}
