package co.cue.mascotas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    private NotificationType tipo;
    private Map<String, String> payload;
}

