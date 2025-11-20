package co.cue.facturas_service.models.dtos.kafka;

import co.cue.facturas_service.models.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    private NotificationType tipo;
    private Map<String, String> payload;
}
