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
    // Template de Kafka que permite enviar mensajes al broker.
    // Spring lo inyecta automáticamente gracias a @RequiredArgsConstructor.
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Nombre del tópico al que se enviarán las notificaciones.
    private static final String TOPIC = "usuarios_registrados_topic";

    public void enviarNotificacion(NotificationRequestDTO request) {
        try {
            // Log informativo indicando que se está enviando el mensaje.
            log.info("Enviando solicitud de notificación [{}] a Kafka...", request.getTipo());

            // Envío del mensaje al tópico definido.
            kafkaTemplate.send(TOPIC, request);
        } catch (Exception e) {
            // Si ocurre un error al enviar el mensaje, se registra en el log
            log.error("Error al enviar notificación a Kafka: {}", e.getMessage());
        }
    }
}
