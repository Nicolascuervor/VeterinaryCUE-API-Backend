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
public class CitaHorarioReasignadoEmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia CITA_HORARIO_REASIGNADO...");
        try {
            Map<String, String> data = request.getPayload();
            
            String correo = data.get("correo");
            String nombreDuenio = data.get("nombreDuenio");
            String nombreMascota = data.get("nombreMascota");
            String fechaAnterior = data.get("fechaAnterior");
            String fechaNueva = data.get("fechaNueva");
            String medico = data.get("medico");

            if (correo == null || nombreDuenio == null || nombreMascota == null || 
                fechaAnterior == null || fechaNueva == null) {
                log.warn("Payload incompleto para Estrategia CITA_HORARIO_REASIGNADO. Faltan datos requeridos.");
                return;
            }

            emailService.enviarNotificacionCitaHorarioReasignado(
                    correo, nombreDuenio, nombreMascota, fechaAnterior, fechaNueva, 
                    medico != null ? medico : "Veterinario");

        } catch (Exception e) {
            log.error("Error al procesar estrategia CITA_HORARIO_REASIGNADO: {}", e.getMessage(), e);
        }
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.CITA_HORARIO_REASIGNADO;
    }
}

