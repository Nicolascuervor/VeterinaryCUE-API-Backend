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
import co.cue.auth.models.entities.Veterinario; // Necesario para 'actualizar'
import jakarta.persistence.EntityNotFoundException; // Para manejar 'Optional'

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final PasswordEncoder passwordEncoder;
    private IAuthService self;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducerService kafkaProducerService; // <-- INYECTAR

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
        this.self = null;
    }

    @Autowired
    public void setSelf(@Lazy IAuthService self) { this.self = self; }

    @Override
    @Transactional
    public Usuario registrarUsuario(RegistroUsuarioDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo " + dto.getCorreo() + " ya está registrado.");
        }
        String contraseniaHasheada = passwordEncoder.encode(dto.getContrasenia());
        dto.setContrasenia(contraseniaHasheada);

        Usuario nuevoUsuario = usuarioFactory.crearUsuario(dto);

        // Guardamos el usuario
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        //
        // 1. Construir el Payload genérico
        Map<String, String> payload = new HashMap<>();
        payload.put("nombre", usuarioGuardado.getNombre());
        payload.put("correo", usuarioGuardado.getCorreo());

        // 2. Construir la solicitud de notificación genérica
        NotificationRequestDTO notificationRequest = new NotificationRequestDTO(
                NotificationType.EMAIL, // <-- Especificamos la ESTRATEGIA que queremos
                payload
        );

        // 3. Enviar el evento genérico
        kafkaProducerService.enviarNotificacion(notificationRequest);

        // 4. Devolvemos el usuario que ya guardamos
        return usuarioGuardado;
    }


    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getCorreo(),
                        dto.getContrasenia()
                )
        );
        UserDetails usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
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

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, ActualizarUsuarioDTO dto) {

        Usuario usuario = self.obtenerUsuarioPorId(id);

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());
        if (usuario instanceof Veterinario vet && dto.getEspecialidad() != null) {
            vet.setEspecialidad(dto.getEspecialidad());
        }
        return usuarioRepository.save(usuario);
    }


    @Override
    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }


}
