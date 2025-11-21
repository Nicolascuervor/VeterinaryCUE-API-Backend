package co.cue.auth.models.kafka;

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
