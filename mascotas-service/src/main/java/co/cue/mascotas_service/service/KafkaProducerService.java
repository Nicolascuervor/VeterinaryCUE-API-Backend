package co.cue.mascotas_service.service;

import co.cue.mascotas_service.dto.NotificationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String NOTIFICACIONES_TOPIC = "usuarios_registrados_topic";

    public void enviarNotificacion(NotificationRequestDTO request) {
        try {
            log.info("Enviando notificación tipo {} a Kafka...", request.getTipo());
            kafkaTemplate.send(NOTIFICACIONES_TOPIC, request);
            log.info("Notificación enviada exitosamente al tópico {}", NOTIFICACIONES_TOPIC);
        } catch (Exception e) {
            log.error("Error al enviar notificación a Kafka", e);
        }
    }
}

