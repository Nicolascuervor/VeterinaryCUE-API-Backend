package co.cue.citas_service.service;

import co.cue.citas_service.events.CitaCompletadaEventDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor

// Servicio para enviar eventos a Kafka
public class KafkaProducerService {

    // Plantilla de Kafka para enviar mensajes
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Topic donde se envían las citas completadas
    public static final String CITAS_COMPLETADAS_TOPIC = "citas_completadas_topic";

    // Método para enviar evento de cita completada
    public void enviarCitaCompletada(CitaCompletadaEventDTO event) {
        try {
            log.info("Enviando evento de Cita Completada (ID: {}) a Kafka, topic: {}",
                    event.getCitaId(), CITAS_COMPLETADAS_TOPIC);
            kafkaTemplate.send(CITAS_COMPLETADAS_TOPIC, event);

        } catch (Exception e) {
            log.error("Error al enviar CitaCompletadaEventDTO a Kafka", e);
        }
    }
}
