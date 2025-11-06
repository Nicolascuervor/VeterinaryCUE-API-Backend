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

/**
 * Este es el Contexto (el Orquestador).
 * Su trabajo es CONOCER todas las estrategias y ELEGIR la correcta.
 * No sabe CÓMO se envía un email, solo sabe DELEGAR.
 */
@Service
@Slf4j
public class NotificationOrchestratorService {

    // (Colega Senior): Este es el "truco" de Spring.
    // Un Mapa que contendrá todas nuestras estrategias (Email, SMS, etc.)
    private final Map<NotificationType, NotificationStrategy> strategies;

    /**
     * Usamos inyección por constructor. Spring nos pasará
     * una LISTA de TODOS los beans que implementen NotificationStrategy.
     * (Es decir, EmailNotificationStrategy y futuros SMSNotificationStrategy).
     *
     * Luego, llenamos el Mapa usando el método getTipo() de cada estrategia
     * como clave.
     */
    @Autowired
    public NotificationOrchestratorService(List<NotificationStrategy> strategyList) {
        strategies = new EnumMap<>(NotificationType.class);
        strategyList.forEach(
                strategy -> strategies.put(strategy.getTipo(), strategy)
        );
        log.info("Estrategias de notificación cargadas: {}", strategies.keySet());
    }

    /**
     * Este es el único método público. Recibe la solicitud
     * y la delega a la estrategia correcta.
     */
    public void procesarNotificacion(NotificationRequestDTO request) {
        if (request == null || request.getTipo() == null) {
            log.warn("Solicitud de notificación inválida o sin tipo.");
            return;
        }

        // 1. Seleccionar la Estrategia
        NotificationStrategy strategy = strategies.get(request.getTipo());

        if (strategy == null) {
            log.error("No se encontró una estrategia para el tipo: {}", request.getTipo());
            // (Opcional): Enviar a una cola de "mensajes muertos" (Dead Letter Queue)
            return;
        }

        // 2. Ejecutar la Estrategia
        log.debug("Delegando al notificador: {}", request.getTipo());
        strategy.enviar(request);
    }
}