package co.cue.pedidos_service.services;

import co.cue.pedidos_service.client.CarritoServiceClient;
import co.cue.pedidos_service.client.InventarioServiceClient;
import co.cue.pedidos_service.exceptions.PedidoProcesamientoException;
import co.cue.pedidos_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.pedidos_service.models.dtos.kafka.PedidoItemEventDTO;
import co.cue.pedidos_service.models.entities.Pedido;
import co.cue.pedidos_service.models.enums.PedidoEstado;
import co.cue.pedidos_service.pasarela.dtos.EventoPagoDTO;
import co.cue.pedidos_service.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoWebhookService {
    /**
     * Dependencias necesarias para procesar el pago:
     * - PedidoRepository: acceso a la BD para buscar/actualizar pedidos.
     * - InventarioServiceClient: comunicación con el microservicio de inventario.
     * - CarritoServiceClient: limpiar el carrito tras completar el pedido.
     * - KafkaProducerService: enviar eventos a Kafka.
     */

    private final PedidoRepository pedidoRepository;
    private final InventarioServiceClient inventarioClient;
    private final CarritoServiceClient carritoClient;
    private final KafkaProducerService kafkaProducer;

    /**
     * Procesa un evento enviado por el webhook de Stripe cuando un pago es exitoso.
     * El flujo incluye:
     * 1. Validar el pago.
     * 2. Buscar el pedido asociado.
     * 3. Verificar que no haya sido completado previamente.
     * 4. Descontar stock en Inventario.
     * 5. Actualizar estado del pedido.
     * 6. Limpiar el carrito del usuario.
     * 7. Enviar evento a Kafka indicando que el pedido fue completado.
     */
    @Transactional
    public void procesarPagoExitoso(EventoPagoDTO evento) {
        // 1. Validación del estado del pago enviado por Stripe
        if (!evento.isPagoExitoso()) {
            log.warn("Evento de Webhook recibido para Pedido ID: {}, pero no fue exitoso. Ignorando.", evento.getPedidoId());
            return;
        }
        log.info("Procesando evento 'Pago Exitoso' para PaymentIntent ID: {}", evento.getPaymentIntentId());
        // 2. Buscar el pedido por PaymentIntent
        Pedido pedido = pedidoRepository.findByStripePaymentIntentId(evento.getPaymentIntentId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado para PaymentIntent: " + evento.getPaymentIntentId()));
// 3. Evitar procesar el mismo pedido dos veces (Stripe puede enviar eventos duplicados)
        if (pedido.getEstado() == PedidoEstado.COMPLETADO) {
            log.warn("El Pedido {} ya estaba completado. Evento duplicado.", pedido.getId());
            return;
        }

        // 4. Intento de descontar inventario en el microservicio correspondiente
        try {
            log.info("Intentando descontar stock para {} items...", pedido.getItems().size());
            inventarioClient.descontarStock(pedido.getItems()).block();
            log.info("Stock descontado correctamente en Inventario.");
        } catch (Exception e) {
            log.error("Error CRÍTICO al descontar stock: {}", e.getMessage());
            throw new PedidoProcesamientoException("Fallo al descontar inventario en el proceso post-pago", e);
        }
// 5. Actualización del estado del pedido en la BD
        pedido.setEstado(PedidoEstado.COMPLETADO);
        pedidoRepository.save(pedido);
        
        // 6. Limpieza del carrito usando el sessionId guardado en el pedido
        try {
            log.info("Limpiando carrito para Usuario ID: {}, Session ID: {}", pedido.getUsuarioId(), pedido.getSessionId());
            carritoClient.limpiarCarrito(pedido.getUsuarioId(), pedido.getSessionId()).block();
            log.info("Carrito limpiado exitosamente.");
        } catch (Exception e) {
            log.error("Error al limpiar el carrito después del pago: {}", e.getMessage(), e);
            // No lanzamos excepción porque el pago ya fue procesado, pero registramos el error
        }

        // 7. Envío del evento a Kafka para informar a otros microservicios
        kafkaProducer.enviarEventoPedidoCompletado(mapToKafkaDTO(pedido));

        log.info("Pedido {} completado y evento enviado a Kafka.", pedido.getId());
    }

    /**
     * Convierte la entidad Pedido en un DTO listo para enviarse a Kafka.
     * Incluye datos del cliente, totales e items del pedido.
     */
    private PedidoCompletadoEventDTO mapToKafkaDTO(Pedido pedido) {
        // Conversión de los items a su versión de evento Kafka
        Set<PedidoItemEventDTO> itemsDTO = pedido.getItems().stream()
                .map(item -> PedidoItemEventDTO.builder()
                        .productoId(item.getProductoId())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .build())
                .collect(Collectors.toSet());


       // Construcción del evento completo
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
