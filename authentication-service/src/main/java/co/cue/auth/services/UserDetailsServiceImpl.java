package co.cue.auth.services;
import co.cue.auth.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de integración con Spring Security para la recuperación de usuarios.
 * Implementa la interfaz estándar UserDetailsService, que es utilizada por el
 * AuthenticationManager durante el proceso de autenticación para cargar los datos
 * del usuario (identidad, contraseña, roles) desde la fuente de datos persistente.
 * Actúa como un adaptador que conecta nuestro repositorio JPA (UsuarioRepository)
 * con el mecanismo de seguridad del framework.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    /**
     * Busca y carga un usuario basándose en su nombre de usuario (username).
     * En nuestro dominio de negocio, el "username" corresponde al correo electrónico.
     * Spring Security invoca este método automáticamente cuando recibe una petición de login.
     * Flujo:
     * 1. Recibe el correo del intento de login.
     * 2. Consulta el repositorio para encontrar el registro correspondiente.
     * 3. Si existe, retorna la entidad Usuario (que implementa UserDetails).
     * 4. Si no existe, lanza la excepción específica que Spring Security maneja como fallo de autenticación.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscamos por correo, ya que es nuestro identificador único de negocio.
        return usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));
    }
}
