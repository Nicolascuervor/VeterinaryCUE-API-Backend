package co.cue.auth.services;

import co.cue.auth.events.UsuarioRegistradoEvent;
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

    /**
     * (Mentor): Conservamos el método antiguo por si se usa en otro lugar,
     * pero idealmente debería ser deprecado y eliminado.
     */
    public void enviarEventoUsuarioRegistrado(UsuarioRegistradoEvent event) {
        try {
            log.info("Enviando evento de registro (LEGACY) para: {}", event.getCorreo());
            kafkaTemplate.send(USUARIOS_REGISTRADOS_TOPIC, event);
        } catch (Exception e) {
            log.error("Error al enviar evento (LEGACY) a Kafka", e);
        }
    }

    /**
     * método genérico que envía nuestro DTO estándar al mismo topic.
     */
    public void enviarNotificacion(NotificationRequestDTO request) {
        try {
            log.info("Enviando solicitud de notificación genérica tipo: {}", request.getTipo());
            kafkaTemplate.send(USUARIOS_REGISTRADOS_TOPIC, request);
        } catch (Exception e) {
            log.error("Error al enviar NotificationRequestDTO a Kafka", e);
        }
    }
}