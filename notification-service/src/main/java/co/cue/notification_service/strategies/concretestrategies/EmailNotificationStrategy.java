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
            // (Mentor): Extraemos los datos que necesitamos del payload genérico.
            String nombre = request.getPayload().get("nombre");
            String correo = request.getPayload().get("correo");

            // Validamos que tengamos los datos necesarios para ESTA estrategia
            if (nombre == null || correo == null) {
                log.warn("Payload incompleto para Estrategia EMAIL. Faltan 'nombre' o 'correo'.");
                return;
            }

            // Llamamos al servicio que sabe cómo enviar correos.
            emailService.enviarCorreoBienvenida(nombre, correo);

        } catch (Exception e) {
            log.error("Error al procesar estrategia EMAIL: {}", e.getMessage(), e);
        }
    }

    @Override
    public NotificationType getTipo() {
        // Le decimos al Factory que esta clase maneja notificaciones EMAIL
        return NotificationType.EMAIL;
    }
}