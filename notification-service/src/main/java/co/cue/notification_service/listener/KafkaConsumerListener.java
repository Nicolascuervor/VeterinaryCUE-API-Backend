package co.cue.notification_service.listener;

import co.cue.notification_service.events.UsuarioRegistradoEvent;
import co.cue.notification_service.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaConsumerListener {
    private final EmailService emailService;
    @KafkaListener(topics = "usuarios_registrados_topic", // (El mismo nombre de tópico del Productor)
            groupId = "notificaciones_bienvenida_group")
    public void handleUsuarioRegistrado(UsuarioRegistradoEvent event) {
        log.info("Evento de usuario registrado recibido para: {}", event.getCorreo());

        emailService.enviarCorreoBienvenida(event.getNombre(), event.getCorreo());
    }
}