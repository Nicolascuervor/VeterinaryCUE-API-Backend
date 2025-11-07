package co.cue.pedidos_service.services;

import co.cue.pedidos_service.client.CarritoServiceClient;
import co.cue.pedidos_service.client.InventarioServiceClient;
import co.cue.pedidos_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.pedidos_service.models.dtos.kafka.PedidoItemEventDTO;
import co.cue.pedidos_service.models.entities.Pedido;
import co.cue.pedidos_service.models.enums.PedidoEstado;
import co.cue.pedidos_service.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor // (Colega Senior): Reemplaza @AllArgsConstructor
public class PedidoWebhookService {

    private final PedidoRepository pedidoRepository;
    private final InventarioServiceClient inventarioClient;
    private final CarritoServiceClient carritoClient;
    private final KafkaProducerService kafkaProducer;



    @Transactional
    public void handleStripeEvent(String payload, String stripeSignature) {


        if (!payload.contains("payment_intent.succeeded")) {
            log.warn("Evento de Stripe recibido, pero no es 'payment_intent.succeeded'. Ignorando.");
            return;
        }

        String paymentIntentId = "pi_MOCK_ID";


        log.info("Procesando evento 'payment_intent.succeeded' para ID: {}", paymentIntentId);

        // Paso 2. Encontrar el Pedido en nuestra BD
        Pedido pedido = pedidoRepository.findByStripePaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado para PaymentIntent: " + paymentIntentId));

        // Paso 3. Idempotencia: Si ya está completado, no hacer nada.
        if (pedido.getEstado() == PedidoEstado.COMPLETADO) {
            log.warn("El Pedido {} ya estaba completado. Evento duplicado.", pedido.getId());
            return;
        }

        // Paso 4. Actualizar Estado
        pedido.setEstado(PedidoEstado.COMPLETADO);
        pedidoRepository.save(pedido);

        // Iniciamos las llamadas asíncronas a los otros servicios (usando los stubs que creamos).

        // Paso 5. Descontar Stock
        inventarioClient.descontarStock(pedido.getItems()).subscribe();

        // Paso 6. Limpiar Carrito
        // (Mentor): Buscamos el ID de sesión (si usuarioId es nulo)
        String sessionId = (pedido.getUsuarioId() == null) ? "SESSION_ID_FALTANTE_EN_PEDIDO" : null; // (Mejora: Guardar sessionId en Pedido si es invitado)
        carritoClient.limpiarCarrito(pedido.getUsuarioId(), sessionId).subscribe();

        // Paso 7. Publicar Evento en Kafka (para Facturas)
        kafkaProducer.enviarEventoPedidoCompletado(
                mapToKafkaDTO(pedido)
        );

        log.info("Pedido {} completado y evento enviado a Kafka.", pedido.getId());
    }

    /**
     * Helper para mapear la Entidad Pedido al DTO de Kafka.
     */
    private PedidoCompletadoEventDTO mapToKafkaDTO(Pedido pedido) {
        Set<PedidoItemEventDTO> itemsDTO = pedido.getItems().stream()
                .map(item -> PedidoItemEventDTO.builder()
                        .productoId(item.getProductoId())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .build())
                .collect(Collectors.toSet());

        return PedidoCompletadoEventDTO.builder()
                .pedidoId(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .clienteNombre(pedido.getClienteNombre())
                .clienteEmail(pedido.getClienteEmail())
                .totalPedido(pedido.getTotalPedido())
                .fechaCreacion(pedido.getCreatedAt())
                .items(itemsDTO)
                .build();
    }
}
