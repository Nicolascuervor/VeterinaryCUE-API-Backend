package co.cue.auth.models.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Este DTO también debe ser idéntico al del notification-service.
 * Es el contrato de mensajería entre nuestros microservicios.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    /**
     * El tipo de estrategia que el notification-service debe ejecutar.
     */
    private NotificationType tipo;

    /**
     * El payload (carga útil) con los datos necesarios.
     * Para EMAIL, esperamos "nombre" y "correo".
     */
    private Map<String, String> payload;
}
