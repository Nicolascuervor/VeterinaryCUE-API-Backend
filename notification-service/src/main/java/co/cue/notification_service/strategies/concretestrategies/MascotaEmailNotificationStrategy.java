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
public class MascotaEmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia MASCOTA_CREADA...");
        try {
            Map<String, String> data = request.getPayload();
            
            String correo = data.get("correo");
            String nombreDuenio = data.get("nombreDuenio");
            String nombreMascota = data.get("nombreMascota");
            String especie = data.get("especie");
            String raza = data.get("raza");

            if (correo == null || nombreDuenio == null || nombreMascota == null || especie == null) {
                log.warn("Payload incompleto para Estrategia MASCOTA_CREADA. Faltan datos requeridos.");
                return;
            }

            emailService.enviarNotificacionMascotaCreada(correo, nombreDuenio, nombreMascota, especie, raza);

        } catch (Exception e) {
            log.error("Error al procesar estrategia MASCOTA_CREADA: {}", e.getMessage(), e);
        }
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.MASCOTA_CREADA;
    }
}

