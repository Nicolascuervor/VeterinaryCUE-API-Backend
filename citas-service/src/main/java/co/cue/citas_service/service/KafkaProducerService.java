package co.cue.citas_service.service;

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
