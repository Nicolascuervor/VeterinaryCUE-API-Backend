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

    // Constantes para claves del payload
    private static final String KEY_CORREO = "correo";
    private static final String KEY_MEDICO = "medico";
    private static final String KEY_NOMBRE_DUENIO = "nombreDuenio";
    private static final String KEY_NOMBRE_MASCOTA = "nombreMascota";
    private static final String KEY_FECHA = "fecha";
    private static final String KEY_TIPO_DESTINATARIO = "tipoDestinatario";
    private static final String KEY_LINK_CONFIRMACION = "linkConfirmacion";
    private static final String TIPO_DESTINATARIO_VETERINARIO = "VETERINARIO";

    private final EmailService emailService;

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia CITA_CONFIRMACION...");
        Map<String, String> data = request.getPayload();
        String tipoDestinatario = data.get(KEY_TIPO_DESTINATARIO);

        if (TIPO_DESTINATARIO_VETERINARIO.equals(tipoDestinatario)) {
            // Enviar correo al veterinario
            emailService.enviarConfirmacionCitaVeterinario(
                    data.get(KEY_CORREO),
                    data.get(KEY_MEDICO),
                    data.get(KEY_NOMBRE_DUENIO),
                    data.get(KEY_NOMBRE_MASCOTA),
                    data.get(KEY_FECHA)
            );
        } else {
            // Enviar correo al dueño (por defecto)
            // Verificar si hay un link de confirmación (para citas nuevas en estado ESPERA)
            String linkConfirmacion = data.get(KEY_LINK_CONFIRMACION);
            if (linkConfirmacion != null && !linkConfirmacion.isEmpty()) {
                // Enviar correo con link de confirmación
                emailService.enviarConfirmacionCitaConLink(
                        data.get(KEY_CORREO),
                        data.get(KEY_NOMBRE_DUENIO),
                        data.get(KEY_NOMBRE_MASCOTA),
                        data.get(KEY_FECHA),
                        data.get(KEY_MEDICO),
                        linkConfirmacion
                );
            } else {
                // Enviar correo de confirmación normal (cita ya confirmada)
                emailService.enviarConfirmacionCita(
                        data.get(KEY_CORREO),
                        data.get(KEY_NOMBRE_DUENIO),
                        data.get(KEY_NOMBRE_MASCOTA),
                        data.get(KEY_FECHA),
                        data.get(KEY_MEDICO)
                );
            }
        }
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.CITA_CONFIRMACION;
    }
}