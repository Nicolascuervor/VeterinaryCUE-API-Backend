package co.cue.pedidos_service.services;

import org.springframework.stereotype.Service;

import co.cue.pedidos_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Service
@Slf4j
@AllArgsConstructor // (Mentor): Inyecta el KafkaTemplate
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public static final String PEDIDOS_COMPLETADOS_TOPIC = "pedidos_completados_topic";

    /**
     * Envía el evento de pedido completado al topic de Kafka.
     */
    public void enviarEventoPedidoCompletado(PedidoCompletadoEventDTO event) {
        try {
            log.info("Enviando evento de Pedido Completado (ID: {}) a Kafka", event.getPedidoId());
            kafkaTemplate.send(PEDIDOS_COMPLETADOS_TOPIC, event);
        } catch (Exception e) {
            log.error("Error al enviar PedidoCompletadoEventDTO a Kafka", e);
        }
    }
}