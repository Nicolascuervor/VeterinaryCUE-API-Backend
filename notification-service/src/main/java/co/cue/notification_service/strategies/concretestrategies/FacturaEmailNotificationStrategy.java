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
public class FacturaEmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia EMAIL_FACTURA...");
        Map<String, String> data = request.getPayload();
        String nombre = data.get("clienteNombre");
        String correo = data.get("clienteEmail");
        String numFactura = data.get("numFactura");
        String total = data.get("total");
        String fecha = data.get("fecha");

        if (nombre == null || correo == null || numFactura == null) {
            log.error("Payload incompleto para Factura. Faltan datos.");
            return;
        }

        emailService.enviarResumenFactura(correo, nombre, numFactura, total, fecha);
    }

    @Override
    public NotificationType getTipo() {
        return NotificationType.FACTURA;
    }
}
