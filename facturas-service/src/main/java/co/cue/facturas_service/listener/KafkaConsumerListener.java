package co.cue.facturas_service.listener;

import co.cue.facturas_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.facturas_service.services.FacturaStrategyOrchestrator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaConsumerListener {

    private final FacturaStrategyOrchestrator orchestrator;

    @KafkaListener(topics = "pedidos_completados_topic",
            groupId = "${spring.kafka.consumer.group-id}")
    public void handlePedidoCompletado(PedidoCompletadoEventDTO evento) {

        log.info("Evento 'PedidoCompletadoEventDTO' recibido en facturas-service para Pedido ID: {}", evento.getPedidoId());

        try {
            orchestrator.procesarGeneracionFactura(evento);
        } catch (Exception e) {
            log.error("Error al procesar la generación de factura para Pedido ID: {}", evento.getPedidoId(), e);

        }
    }

}
