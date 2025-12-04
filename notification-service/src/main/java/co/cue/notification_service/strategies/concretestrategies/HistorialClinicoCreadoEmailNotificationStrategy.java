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
public class HistorialClinicoCreadoEmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia HISTORIAL_CLINICO_CREADO...");
        try {
            Map<String, String> data = request.getPayload();
            
            String correo = data.get("correo");
            String nombreDuenio = data.get("nombreDuenio");
            String nombreMascota = data.get("nombreMascota");
            String fecha = data.get("fecha");
            String diagnostico = data.get("diagnostico");

            if (correo == null || nombreDuenio == null || nombreMascota == null || fecha == null) {
                log.warn("Payload incompleto para Estrategia HISTORIAL_CLINICO_CREADO. Faltan datos requeridos.");
                return;
            }

            emailService.enviarNotificacionHistorialClinicoCreado(correo, nombreDuenio, nombreMascota, fecha, diagnostico);

        } catch (Exception e) {
            log.error("Error al procesar estrategia HISTORIAL_CLINICO_CREADO: {}", e.getMessage(), e);
        }
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.HISTORIAL_CLINICO_CREADO;
    }
}

