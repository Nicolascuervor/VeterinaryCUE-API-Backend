package co.cue.auth.services;

import co.cue.auth.models.kafka.NotificationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public static final String USUARIOS_REGISTRADOS_TOPIC = "usuarios_registrados_topic";

    public void enviarNotificacion(NotificationRequestDTO request) {
        try {
            log.info("Enviando solicitud de notificación genérica tipo: {}", request.getTipo());
            kafkaTemplate.send(USUARIOS_REGISTRADOS_TOPIC, request);
        } catch (Exception e) {
            log.error("Error al enviar NotificationRequestDTO a Kafka", e);
        }
    }
}