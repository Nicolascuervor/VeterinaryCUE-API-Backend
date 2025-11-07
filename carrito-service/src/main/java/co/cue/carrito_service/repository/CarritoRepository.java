package co.cue.carrito_service.repository;

import co.cue.carrito_service.models.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Métodos necesarios para buscar el carrito
    Optional<Carrito> findByUsuarioId(Long usuarioId);
    Optional<Carrito> findBySessionId(String sessionId);
}