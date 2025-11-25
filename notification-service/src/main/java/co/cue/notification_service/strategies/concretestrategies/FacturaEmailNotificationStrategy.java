package co.cue.notification_service.strategies.concretestrategies;

import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;
import co.cue.notification_service.services.EmailService;
import co.cue.notification_service.strategies.NotificationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Estrategia concreta para el envío de notificaciones de facturación.
 * Esta clase implementa la lógica especializada para procesar y enviar comprobantes
 * de pago o facturas electrónicas por correo.
 * A diferencia de la estrategia de email estándar, esta implementación espera
 * un conjunto de datos estructurados específicos en el payload (monto, número de factura, fecha)
 * y utiliza una plantilla de correo diferente diseñada para información financiera.
 */
@Component
@AllArgsConstructor
@Slf4j
public class FacturaEmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    /**
     * Procesa la solicitud de notificación de factura.
     * Extrae los datos financieros del mapa genérico y valida que la información crítica
     * esté presente antes de intentar el envío.
     * Datos esperados en el payload:
     * - clienteNombre: Nombre del receptor.
     * - clienteEmail: Correo de destino.
     * - numFactura: Identificador único del comprobante.
     * - total: Monto total de la transacción.
     * - fecha: Fecha de emisión.
     */
    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia EMAIL_FACTURA...");

        // Extracción de Datos Específicos
        // Recuperamos los valores usando las claves acordadas con el servicio de facturación.
        Map<String, String> data = request.getPayload();
        String nombre = data.get("clienteNombre");
        String correo = data.get("clienteEmail");
        String numFactura = data.get("numFactura");
        String total = data.get("total");
        String fecha = data.get("fecha");

        // Validación de Integridad
        // Si faltan datos esenciales para generar el comprobante, abortamos la operación
        // para evitar enviar un correo con información vacía o corrupta ("null").
        if (nombre == null || correo == null || numFactura == null) {
            log.error("Payload incompleto para Factura. Faltan datos.");
            return;
        }

        // Ejecución del Envío
        // Invocamos el método especializado del servicio de correo que sabe cómo
        // formatear el texto de una factura.
        emailService.enviarResumenFactura(correo, nombre, numFactura, total, fecha);
    }

    /**
     * Retorna el identificador del tipo de notificación que esta estrategia maneja.
     * Devuelve NotificationType.FACTURA, permitiendo al orquestador enrutar
     * los eventos de facturación hacia esta lógica específica.
     */
    @Override
    public NotificationType getTipo() {
        return NotificationType.FACTURA;
    }
}