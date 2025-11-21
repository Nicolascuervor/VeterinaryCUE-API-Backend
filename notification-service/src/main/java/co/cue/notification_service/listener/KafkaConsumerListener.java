package co.cue.notification_service.listener;


import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.services.NotificationOrchestratorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaConsumerListener {

    private final NotificationOrchestratorService orchestratorService;

    @KafkaListener(topics = "usuarios_registrados_topic",
            groupId = "notificaciones_bienvenida_group")

    public void handleNotificationRequest(NotificationRequestDTO event) {
        log.info("Evento de notificación recibido para tipo: {}", event.getTipo());

        orchestratorService.procesarNotificacion(event);
    }
}