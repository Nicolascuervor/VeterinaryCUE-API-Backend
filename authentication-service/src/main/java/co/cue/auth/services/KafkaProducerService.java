package co.cue.auth.services;

import co.cue.auth.events.UsuarioRegistradoEvent;
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

    public void enviarEventoUsuarioRegistrado(UsuarioRegistradoEvent event) {
        try {
            log.info("Enviando evento de registro para: {}", event.getCorreo());
            // (Mentor): Enviamos el DTO. Spring lo convertirá a JSON automáticamente.
            kafkaTemplate.send(USUARIOS_REGISTRADOS_TOPIC, event);
        } catch (Exception e) {
            log.error("Error al enviar evento a Kafka", e);
        }
    }
}
