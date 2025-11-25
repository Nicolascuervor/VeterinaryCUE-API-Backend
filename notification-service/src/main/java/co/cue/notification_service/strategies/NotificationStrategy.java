package co.cue.notification_service.strategies;


import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;


/**
 * Interfaz que define el contrato para las estrategias de envío de notificaciones.
 * Implementa el patrón de diseño Strategy. Cada clase que implemente esta interfaz
 * encapsulará la lógica específica para enviar una notificación a través de un
 * canal concreto (por ejemplo, Correo Electrónico, SMS, Push Notification).
 * Esto permite que el orquestador (NotificationOrchestratorService) delegue el envío
 * sin conocer los detalles técnicos de cada medio de comunicación, facilitando
 * la extensión del sistema con nuevos canales en el futuro.
 */
public interface NotificationStrategy {

    /**
     * Ejecuta la lógica de envío de la notificación.
     * Este método toma el DTO de solicitud, extrae los datos necesarios del payload
     * (como destinatario, mensaje, asunto) y utiliza los servicios de infraestructura
     * (como EmailService) para transmitir el mensaje.
     */
    void enviar(NotificationRequestDTO request);

    /**
     * Obtiene el identificador del tipo de notificación que maneja esta estrategia.
     * Este método es utilizado por el NotificationOrchestratorService para registrar
     * automáticamente esta estrategia en su mapa interno. Actúa como la "clave"
     * que asocia un valor del enum NotificationType con esta implementación concreta.
     */
    NotificationType getTipo();
}
