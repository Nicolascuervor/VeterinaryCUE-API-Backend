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
public class CitaEmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia CITA_CONFIRMACION...");
        Map<String, String> data = request.getPayload();
        String tipoDestinatario = data.get("tipoDestinatario");

        if ("VETERINARIO".equals(tipoDestinatario)) {
            // Enviar correo al veterinario
            emailService.enviarConfirmacionCitaVeterinario(
                    data.get("correo"),
                    data.get("medico"), // Nombre del veterinario
                    data.get("nombreDuenio"),
                    data.get("nombreMascota"),
                    data.get("fecha")
            );
        } else {
            // Enviar correo al dueño (por defecto)
            emailService.enviarConfirmacionCita(
                    data.get("correo"),
                    data.get("nombreDuenio"),
                    data.get("nombreMascota"),
                    data.get("fecha"),
                    data.get("medico")
            );
        }
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.CITA_CONFIRMACION;
    }
}