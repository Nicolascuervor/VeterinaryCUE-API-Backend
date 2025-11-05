package co.cue.auth.strategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.enums.TipoUsuario;

public interface IUsuarioCreationStrategy {
    Usuario crearUsuario(RegistroUsuarioDTO dto);
    TipoUsuario getTipo();
}