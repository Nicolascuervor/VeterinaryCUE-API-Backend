package co.cue.pedidos_service.repository;

import co.cue.pedidos_service.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByStripePaymentIntentId(String paymentIntentId);
}
