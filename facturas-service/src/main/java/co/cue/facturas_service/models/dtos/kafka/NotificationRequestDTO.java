package co.cue.facturas_service.models.dtos.kafka;

import co.cue.facturas_service.models.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

@Getter  // Genera automáticamente los métodos getter para todos los atributos.
@Setter  // Genera automáticamente los métodos setter para todos los atributos.
@AllArgsConstructor   // Genera un constructor con todos los atributos de la clase.
@NoArgsConstructor    // Genera un constructor vacío (sin parámetros).
public class NotificationRequestDTO {
    // Tipo de notificación que se desea enviar.
    // Puede ser un enum como EMAIL, PUSH, SMS, etc.
    private NotificationType tipo;

    // Datos adicionales necesarios para procesar la notificación.
    // Se almacenan en un mapa clave-valor para permitir flexibilidad.
    private Map<String, String> payload;
}
