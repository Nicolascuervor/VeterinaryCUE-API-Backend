package co.cue.auth.repository;

import co.cue.auth.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    Optional<Usuario> findByIdAndActivoTrue(Long id);

    Optional<Usuario> findByCorreoAndActivoTrue(String correo);

    List<Usuario> findAllByActivoTrue();
}
