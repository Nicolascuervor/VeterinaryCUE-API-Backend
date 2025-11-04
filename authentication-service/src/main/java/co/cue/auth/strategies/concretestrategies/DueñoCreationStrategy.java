package co.cue.auth.strategies.concretestrategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Duenio;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.enums.TipoUsuario;
import co.cue.auth.strategies.IUsuarioCreationStrategy;
import org.springframework.stereotype.Component;

@Component
public class DueñoCreationStrategy implements IUsuarioCreationStrategy {

    @Override
    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        return new Duenio(
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getContraseña(),
                dto.getDireccion(),
                dto.getTelefono()
        );
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.DUEÑO;
    }
}