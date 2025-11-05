package co.cue.auth.controller;

import co.cue.auth.models.dtos.ActualizarUsuarioDTO;
import co.cue.auth.models.dtos.AuthResponseDTO;
import co.cue.auth.models.dtos.LoginRequestDTO;
import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.services.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final IAuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody RegistroUsuarioDTO dto) {
        Usuario usuarioRegistrado = authService.registrarUsuario(dto);
        return new ResponseEntity<>(usuarioRegistrado, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos() {
        List<Usuario> usuarios = authService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = authService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody ActualizarUsuarioDTO dto) {
        Usuario usuarioActualizado = authService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        authService.desactivarUsuario(id);
        // (Mentor): Para un DELETE exitoso, devolvemos 204 No Content
        return ResponseEntity.noContent().build();
    }
}