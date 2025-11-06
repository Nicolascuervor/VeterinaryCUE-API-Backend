package co.cue.notification_service.models.dtos.requestdtos;

import co.cue.notification_service.models.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 *    DTO genérico para la cola de Kafka.
 *
 * - tipo: Le dice a nuestro servicio qué estrategia usar (EMAIL, SMS, etc.).
 * - payload: Un mapa flexible que contiene los datos que CADA estrategia necesita.
 * Para EMAIL, contendrá "nombre" y "correo".
 * Para SMS, podría contener "telefono" y "codigo".
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    private NotificationType tipo;
    private Map<String, String> payload;
}
