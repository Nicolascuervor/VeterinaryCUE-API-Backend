package co.cue.auth.services;
import co.cue.auth.models.dtos.ActualizarUsuarioDTO;
import co.cue.auth.models.dtos.AuthResponseDTO;
import co.cue.auth.models.dtos.LoginRequestDTO;
import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;

import java.util.List;

public interface IAuthService {
    Usuario registrarUsuario(RegistroUsuarioDTO dto);
    List<Usuario> obtenerUsuariosActivos();
    Usuario obtenerUsuarioPorCorreo(String correo);
    Usuario obtenerUsuarioPorId(Long id);
    Usuario actualizarUsuario(Long id, ActualizarUsuarioDTO dto);
    void desactivarUsuario(Long id);
    AuthResponseDTO login(LoginRequestDTO dto);
}
