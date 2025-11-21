package co.cue.notification_service.strategies;


import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;


public interface NotificationStrategy {

    void enviar(NotificationRequestDTO request);
    NotificationType getTipo();
}
