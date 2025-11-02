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
        // (Mentor): Ignoramos 'especialidad' o cualquier otro campo que no aplique.
        return new Admin(
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
        return TipoUsuario.ADMIN; // <-- Debes añadir ADMIN a tu enum TipoUsuario.java
    }
}
