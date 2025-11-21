package co.cue.historias_clinicas_service.listener;

import co.cue.historias_clinicas_service.events.CitaCompletadaEventDTO;
import co.cue.historias_clinicas_service.service.IHistorialClinicoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaConsumerListener {
    private final IHistorialClinicoService historialClinicoService;
    @KafkaListener(
            topics = "citas_completadas_topic",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleCitaCompletada(CitaCompletadaEventDTO evento) {

        log.info("Evento 'CitaCompletadaEventDTO' recibido para Cita ID: {}", evento.getCitaId());

        try {
            historialClinicoService.registrarHistorialDesdeEvento(evento);

        } catch (Exception e) {
            log.error("Error al procesar CitaCompletadaEventDTO para Cita ID: {}",
                    evento.getCitaId(), e);
        }
    }
}