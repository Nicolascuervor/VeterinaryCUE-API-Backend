package co.cue.auth.controller;

import co.cue.auth.models.dtos.ActualizarUsuarioDTO;
import co.cue.auth.models.dtos.AuthResponseDTO;
import co.cue.auth.models.dtos.LoginRequestDTO;
import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.services.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



/**
 * Controlador REST para la gestión de autenticación y usuarios.
 * Expone los endpoints necesarios para el ciclo de vida de los usuarios en el sistema:
 * Registro de nuevos usuarios (Dueños, Veterinarios, Admins).
 * Inicio de sesión (Login) y emisión de tokens JWT.
 * Consultas y actualizaciones de perfiles de usuario.
 * Desactivación (Soft Delete) de cuentas.
 * Actúa como la capa de entrada que delega la lógica de negocio al IAuthService.
 */

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500"}) // Configuración CORS local específica para este controlador
public class AuthController {

    private final IAuthService authService;

    /**
     * Registra un nuevo usuario en el sistema.
     * Endpoint protegido (generalmente para ADMIN) que crea una nueva cuenta.
     * Valida los datos de entrada usando las anotaciones de Jakarta Validation (@Valid).
     */
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody RegistroUsuarioDTO dto) {
        Usuario usuarioRegistrado = authService.registrarUsuario(dto);
        return new ResponseEntity<>(usuarioRegistrado, HttpStatus.CREATED);
    }

    /**
     * Autentica a un usuario y genera un token de acceso.
     * Endpoint público. Verifica las credenciales (correo y contraseña).
     * Si son válidas, retorna un token JWT que el cliente debe usar en futuras peticiones.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los usuarios que se encuentran activos en el sistema.
     * Endpoint administrativo para obtener una visión general de los usuarios.
     * Filtra aquellos que han sido desactivados lógicamente.
     */
    @GetMapping("/active/users")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos() {
        List<Usuario> usuarios = authService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca un usuario específico por su identificador único (ID).
     * Utilizado para cargar perfiles de usuario o validar existencias.
     * Requiere autenticación previa.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = authService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * Útil para validaciones de unicidad o recuperación de cuentas.
     */
    @GetMapping("/email")
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@RequestParam String correo) {
        Usuario usuario = authService.obtenerUsuarioPorCorreo(correo);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualiza la información de un usuario existente.
     * Permite modificar datos básicos como dirección o teléfono.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody ActualizarUsuarioDTO dto) {
        Usuario usuarioActualizado = authService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Desactiva (elimina lógicamente) a un usuario del sistema.
     * En lugar de borrar el registro de la base de datos (lo cual rompería integridad referencial
     * con citas o facturas históricas), se marca el campo 'activo' como false.
     * Endpoint restringido a administradores.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        authService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload-photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String url = authService.subirFotoPerfil(id, file);
            return ResponseEntity.ok(new AuthResponseDTO("Foto actualizada correctamente", url, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir la imagen: " + e.getMessage());
        }
    }
}