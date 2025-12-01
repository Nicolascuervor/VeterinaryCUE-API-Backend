package co.cue.citas_service.dtos;
import co.cue.citas_service.dtos.enums.NotificationType;
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
