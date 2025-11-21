package co.cue.notification_service.services;


import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;
import co.cue.notification_service.strategies.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class NotificationOrchestratorService {
    private final Map<NotificationType, NotificationStrategy> strategies;
    @Autowired
    public NotificationOrchestratorService(List<NotificationStrategy> strategyList) {
        strategies = new EnumMap<>(NotificationType.class);
        strategyList.forEach(
                strategy -> strategies.put(strategy.getTipo(), strategy)
        );
        log.info("Estrategias de notificación cargadas: {}", strategies.keySet());
    }
    public void procesarNotificacion(NotificationRequestDTO request) {
        if (request == null || request.getTipo() == null) {
            log.warn("Solicitud de notificación inválida o sin tipo.");
            return;
        }
        NotificationStrategy strategy = strategies.get(request.getTipo());
        if (strategy == null) {
            log.error("No se encontró una estrategia para el tipo: {}", request.getTipo());
            return;
        }
        log.debug("Delegando al notificador: {}", request.getTipo());
        strategy.enviar(request);
    }
}