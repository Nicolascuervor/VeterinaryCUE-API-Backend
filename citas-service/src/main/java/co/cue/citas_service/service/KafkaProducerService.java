package co.cue.citas_service.service;
import co.cue.citas_service.dtos.NotificationRequestDTO; // Importa el nuevo DTO
import co.cue.citas_service.events.CitaCompletadaEventDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String CITAS_COMPLETADAS_TOPIC = "citas_completadas_topic";
    // Tópico compartido para notificaciones (mismo que escucha notification-service)
    public static final String NOTIFICACIONES_TOPIC = "usuarios_registrados_topic";

    public void enviarCitaCompletada(CitaCompletadaEventDTO event) {
        try {
            log.info("Enviando evento de Cita Completada (ID: {}) a Kafka", event.getCitaId());
            kafkaTemplate.send(CITAS_COMPLETADAS_TOPIC, event);
            log.info("Evento de Cita Completada enviado exitosamente al tópico {}", CITAS_COMPLETADAS_TOPIC);
        } catch (Exception e) {
            log.error("Error al enviar CitaCompletadaEventDTO a Kafka para Cita ID: {}", event.getCitaId(), e);
        }
    }

    /**
     * Nuevo método para enviar solicitudes de notificación (Email, SMS, etc.)
     */
    public void enviarNotificacion(NotificationRequestDTO request) {
        try {
            log.info("Enviando notificación tipo {} a Kafka...", request.getTipo());
            kafkaTemplate.send(NOTIFICACIONES_TOPIC, request);
        } catch (Exception e) {
            log.error("Error al enviar notificación a Kafka", e);
        }
    }
}