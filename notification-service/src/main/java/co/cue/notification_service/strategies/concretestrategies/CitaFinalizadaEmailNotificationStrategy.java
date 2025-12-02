package co.cue.notification_service.strategies.concretestrategies;

import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;
import co.cue.notification_service.services.EmailService;
import co.cue.notification_service.strategies.NotificationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class CitaFinalizadaEmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia CITA_FINALIZADA...");
        try {
            Map<String, String> data = request.getPayload();
            
            emailService.enviarNotificacionCitaFinalizada(
                    data.get("correo"),
                    data.get("nombreDuenio"),
                    data.get("nombreMascota"),
                    data.get("fecha"),
                    data.get("medico")
            );
        } catch (Exception e) {
            log.error("Error al procesar estrategia CITA_FINALIZADA: {}", e.getMessage(), e);
        }
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.CITA_FINALIZADA;
    }
}

