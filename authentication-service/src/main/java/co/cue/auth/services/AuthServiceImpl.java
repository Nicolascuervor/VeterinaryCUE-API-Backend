package co.cue.auth.services;
import co.cue.auth.models.dtos.AuthResponseDTO;
import co.cue.auth.models.dtos.LoginRequestDTO;
import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.kafka.NotificationRequestDTO;
import co.cue.auth.models.kafka.NotificationType;
import co.cue.auth.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.cue.auth.models.dtos.ActualizarUsuarioDTO;
import co.cue.auth.models.entities.Veterinario;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementación de la lógica de negocio para la autenticación y gestión de usuarios.
 *
 * Esta clase actúa como el orquestador principal del microservicio.
 * Coordina la interacción entre:
 * - Persistencia (UsuarioRepository)
 * - Seguridad (PasswordEncoder, AuthenticationManager, JwtService)
 * - Creación de Objetos (UsuarioFactory)
 * - Mensajería Asíncrona (KafkaProducerService)
 *
 * Se encarga de garantizar que las reglas de negocio (como no duplicar correos o
 * encriptar contraseñas antes de guardar) se cumplan estrictamente.
 */
@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final PasswordEncoder passwordEncoder;

    // Referencia al propio proxy del servicio (Self-Injection).
    // Se usa para llamar métodos internos (como obtenerUsuarioPorId) asegurando que
    // la intercepción de Spring (AOP) para transacciones (@Transactional) se ejecute.
    private IAuthService self;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducerService kafkaProducerService;

    /**
     * Constructor manual para manejar la inyección cíclica con @Lazy.
     *
     * Spring no permite que un Bean se inyecte a sí mismo directamente en el constructor
     * estándar de Lombok porque crearía un ciclo infinito al inicializarse.
     * Rompemos el ciclo inyectando 'self' posteriormente o marcándolo como Lazy.
     */
    @Autowired
    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           UsuarioFactory usuarioFactory,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager, KafkaProducerService kafkaProducerService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioFactory = usuarioFactory;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.kafkaProducerService = kafkaProducerService;
        this.self = null; // Se inicializará en el setter
    }

    /**
     * Setter para la inyección diferida (Lazy) del proxy de este servicio.
     */
    @Autowired
    public void setSelf(@Lazy IAuthService self) { this.self = self; }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Flujo del proceso:
     * 1. Validación de Negocio: Verifica que el correo no exista previamente.
     * 2. Seguridad: Encripta la contraseña usando BCrypt (nunca se guarda en texto plano).
     * 3. Construcción: Delega al Factory la creación de la entidad correcta (Veterinario, Admin, etc.).
     * 4. Persistencia: Guarda el usuario en la base de datos.
     * 5. Notificación: Envía un evento asíncrono a Kafka para el correo de bienvenida (Side Effect).
     */
    @Override
    @Transactional
    public Usuario registrarUsuario(RegistroUsuarioDTO dto) {
        // Validación de Unicidad
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo " + dto.getCorreo() + " ya está registrado.");
        }

        // Hashing de Contraseña
        String contraseniaHasheada = passwordEncoder.encode(dto.getContrasenia());
        dto.setContrasenia(contraseniaHasheada);

        // Patrón Factory
        // La fábrica decide qué subclase instanciar basándose en el TipoUsuario del DTO.
        Usuario nuevoUsuario = usuarioFactory.crearUsuario(dto);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // Comunicación Asíncrona (Kafka)
        // Preparamos el payload genérico para el servicio de notificaciones.
        Map<String, String> payload = new HashMap<>();
        payload.put("nombre", usuarioGuardado.getNombre());
        payload.put("correo", usuarioGuardado.getCorreo());

        NotificationRequestDTO notificationRequest = new NotificationRequestDTO(
                NotificationType.EMAIL,
                payload
        );

        // Enviamos el mensaje "Fire-and-Forget" (dispara y olvida) a la cola.
        kafkaProducerService.enviarNotificacion(notificationRequest);

        return usuarioGuardado;
    }

    /**
     * Autentica a un usuario y genera un token de sesión.
     *
     * Utiliza el AuthenticationManager de Spring Security para verificar las credenciales.
     * Si la autenticación falla, Spring lanzará una excepción (AuthenticationException)
     * que será capturada por el GlobalExceptionHandler.
     */
    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        // Delega la validación de contraseña (comparar plano vs hash) al proveedor configurado.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getCorreo(),
                        dto.getContrasenia()
                )
        );

        // Si llegamos aquí, la autenticación fue exitosa. Recuperamos al usuario completo.
        UserDetails usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Generamos el token firmado.
        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findAllByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreoAndActivoTrue(correo)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con correo: " + correo));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Actualiza la información de perfil de un usuario.
     */
    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, ActualizarUsuarioDTO dto) {
        // Usamos 'self' para invocar el método transacciónal de búsqueda.
        Usuario usuario = self.obtenerUsuarioPorId(id);

        // Actualizamos campos comunes
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());

        // Lógica Polimórfica
        // Verificamos si es una instancia de Veterinario para actualizar campos específicos.
        // Utilizamos 'Pattern Matching for instanceof' (Java 16+) para evitar el cast explícito.
        if (usuario instanceof Veterinario vet && dto.getEspecialidad() != null) {
            vet.setEspecialidad(dto.getEspecialidad());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Realiza una eliminación lógica (Soft Delete) del usuario.
     *
     * Cambia el estado 'activo' a false en lugar de borrar el registro físico,
     * preservando la integridad referencial de datos históricos (citas, facturas).
     */
    @Override
    @Transactional
    public void desactivarUsuario(Long id) {
        // Buscamos incluso si ya está inactivo, para evitar errores de "No encontrado" si se reintenta.
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}
