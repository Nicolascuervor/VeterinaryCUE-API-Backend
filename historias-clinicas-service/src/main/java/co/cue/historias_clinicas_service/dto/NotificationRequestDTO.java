package co.cue.historias_clinicas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO para enviar notificaciones a través de Kafka.
 * Debe coincidir con la estructura de NotificationRequestDTO del notification-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private NotificationType tipo;
    private Map<String, String> payload;
}

