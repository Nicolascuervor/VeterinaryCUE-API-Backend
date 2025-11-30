package co.cue.carrito_service.repository;

import co.cue.carrito_service.models.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repositorio para la entidad Carrito
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Busca un carrito por el ID del usuario
    Optional<Carrito> findByUsuarioId(Long usuarioId);

    // Busca un carrito por el ID de la sesión
    Optional<Carrito> findBySessionId(String sessionId);
}