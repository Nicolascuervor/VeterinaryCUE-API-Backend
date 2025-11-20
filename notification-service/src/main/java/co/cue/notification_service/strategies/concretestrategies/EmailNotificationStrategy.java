package co.cue.notification_service.strategies.concretestrategies;

import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;
import co.cue.notification_service.services.EmailService;
import co.cue.notification_service.strategies.NotificationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService; // Reutilizamos el servicio existente

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia EMAIL...");
        try {
            String nombre = request.getPayload().get("nombre");
            String correo = request.getPayload().get("correo");
            if (nombre == null || correo == null) {
                log.warn("Payload incompleto para Estrategia EMAIL. Faltan 'nombre' o 'correo'.");
                return;
            }
            emailService.enviarCorreoBienvenida(nombre, correo);

        } catch (Exception e) {
            log.error("Error al procesar estrategia EMAIL: {}", e.getMessage(), e);
        }
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.EMAIL;
    }
}