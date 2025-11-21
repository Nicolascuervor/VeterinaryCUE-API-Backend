package co.cue.auth.strategies.concretestrategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Admin;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.enums.TipoUsuario;
import co.cue.auth.strategies.IUsuarioCreationStrategy;
import org.springframework.stereotype.Component;

@Component
public class AdminCreationStrategy implements IUsuarioCreationStrategy {

    @Override
    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        return new Admin(
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getContrasenia(),
                dto.getDireccion(),
                dto.getTelefono()
        );
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.ADMIN;
    }
}
