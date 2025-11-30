package co.cue.pedidos_service.repository;

import co.cue.pedidos_service.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio JPA para la entidad {@link Pedido}.
 * <p>
 * Esta interfaz permite realizar operaciones CRUD y consultas estándar
 * sobre la tabla "pedidos". Spring Data JPA genera automáticamente
 * la implementación en tiempo de ejecución.
 *
 * Métodos heredados desde {@link JpaRepository}:
 * - save(Pedido pedido)
 * - findById(Long id)
 * - findAll()
 * - deleteById(Long id)
 * - existsById(Long id)
 * entre otros.
 */
@Repository // Marca esta interfaz como componente de acceso a datos para Spring
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    /**
     * Busca un pedido usando el ID del PaymentIntent generado por Stripe.
     *
     * @param paymentIntentId ID único generado por Stripe para un pago.
     * @return Optional con el pedido encontrado o vacío si no existe.
     */
    Optional<Pedido> findByStripePaymentIntentId(String paymentIntentId);
}
