package co.cue.auth.strategies.concretestrategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.entities.Veterinario;
import co.cue.auth.models.enums.TipoUsuario;
import co.cue.auth.strategies.IUsuarioCreationStrategy;
import org.springframework.stereotype.Component;

@Component
public class VeterinarioCreationStrategy implements IUsuarioCreationStrategy {

    @Override
    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        // La misma lógica de validación de antes
        if (dto.getEspecialidad() == null || dto.getEspecialidad().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es requerida para el tipo VETERINARIO");
        }

        return new Veterinario(
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getContrasenia(),
                dto.getDireccion(),
                dto.getTelefono(),
                dto.getEspecialidad()
        );
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.VETERINARIO;
    }
}
