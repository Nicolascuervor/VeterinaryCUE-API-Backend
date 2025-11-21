package co.cue.pedidos_service.services;

import co.cue.pedidos_service.client.CarritoServiceClient;
import co.cue.pedidos_service.client.InventarioServiceClient;
import co.cue.pedidos_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.pedidos_service.models.dtos.kafka.PedidoItemEventDTO;
import co.cue.pedidos_service.models.entities.Pedido;
import co.cue.pedidos_service.models.enums.PedidoEstado;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.repository.PedidoRepository;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@Slf4j
@RequiredArgsConstructor // (Colega Senior): Reemplaza @AllArgsConstructor
public class PedidoWebhookService {

    private final PedidoRepository pedidoRepository;
    private final InventarioServiceClient inventarioClient;
    private final CarritoServiceClient carritoClient;
    private final KafkaProducerService kafkaProducer;


    @Transactional
    public void procesarPagoExitoso(EventoPagoDTO evento) {
        if (!evento.isPagoExitoso()) {
            log.warn("Evento de Webhook recibido para Pedido ID: {}, pero no fue exitoso. Ignorando.", evento.getPedidoId());
            return;
        }
        log.info("Procesando evento 'Pago Exitoso' para PaymentIntent ID: {}", evento.getPaymentIntentId());

        // Usamos el ID de la transacción de la pasarela para encontrar el pedido
        Pedido pedido = pedidoRepository.findByStripePaymentIntentId(evento.getPaymentIntentId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado para PaymentIntent: " + evento.getPaymentIntentId()));

        if (pedido.getEstado() == PedidoEstado.COMPLETADO) {
            log.warn("El Pedido {} ya estaba completado. Evento duplicado.", pedido.getId());
            return;
        }


        try {
            log.info("Intentando descontar stock para {} items...", pedido.getItems().size());
            inventarioClient.descontarStock(pedido.getItems()).block();

            log.info("Stock descontado correctamente en Inventario.");
        } catch (Exception e) {
            log.error("Error CRÍTICO al descontar stock: {}", e.getMessage());
            throw new RuntimeException("Fallo al descontar inventario", e);
        }

        pedido.setEstado(PedidoEstado.COMPLETADO);
        pedidoRepository.save(pedido);
        String sessionId = (pedido.getUsuarioId() == null) ? "SESSION_ID_FALTANTE_EN_PEDIDO" : null;
        try {
            carritoClient.limpiarCarrito(pedido.getUsuarioId(), sessionId).block();
        } catch (Exception e) {
            log.warn("No se pudo limpiar el carrito (no crítico): {}", e.getMessage());
        }
        kafkaProducer.enviarEventoPedidoCompletado(mapToKafkaDTO(pedido));

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
