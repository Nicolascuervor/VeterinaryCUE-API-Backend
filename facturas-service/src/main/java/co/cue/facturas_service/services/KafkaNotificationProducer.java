package co.cue.facturas_service.services;

import co.cue.facturas_service.models.dtos.kafka.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "usuarios_registrados_topic";

    public void enviarNotificacion(NotificationRequestDTO request) {
        try {
            log.info("Enviando solicitud de notificación [{}] a Kafka...", request.getTipo());
            kafkaTemplate.send(TOPIC, request);
        } catch (Exception e) {
            log.error("Error al enviar notificación a Kafka: {}", e.getMessage());
        }
    }
}
