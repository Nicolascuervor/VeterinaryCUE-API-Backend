package co.cue.carrito_service.repository;

import co.cue.carrito_service.models.entities.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repositorio para la entidad ItemCarrito
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    // JpaRepository ya provee métodos CRUD básicos
}