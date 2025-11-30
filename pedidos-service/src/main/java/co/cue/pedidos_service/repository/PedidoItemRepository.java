package co.cue.pedidos_service.repository;

import co.cue.pedidos_service.models.entities.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio JPA para la entidad {@link PedidoItem}.
 * <p>
 * Esta interfaz permite realizar operaciones CRUD y consultas estándar
 * sobre la tabla "pedidos_items" sin necesidad de implementar métodos manualmente.
 * Extiende {@link JpaRepository}, lo cual proporciona métodos como:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 * - existsById()
 * y muchos más.
 */
@Repository  // Indica que esta interfaz es un componente de acceso a datos gestionado por Spring
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {} // No se requieren métodos adicionales; JpaRepository proporciona todos los básicos.
// Si en el futuro se necesitan consultas personalizadas, se pueden agregar aquí.

