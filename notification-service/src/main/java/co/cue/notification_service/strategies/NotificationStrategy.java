package co.cue.notification_service.strategies;


import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;

/**
 * Esta es la interfaz Strategy (nuestro contrato).
 * Define dos métodos:
 * 1. enviar(): El trabajo que debe hacer (enviar la notificación).
 * 2. getTipo(): Cómo se identifica esta estrategia (para que el Factory la encuentre).
 */
public interface NotificationStrategy {

    /**
     * Procesa y envía la notificación usando los datos del DTO.
     */
    void enviar(NotificationRequestDTO request);

    /**
     * Devuelve el tipo de notificación que esta estrategia maneja.
     */
    NotificationType getTipo();
}
