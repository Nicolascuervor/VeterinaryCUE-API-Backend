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
 * Servicio orquestador que gestiona el envío de notificaciones.
 * Este componente actúa como el despachador central del patrón Strategy.
 * Su responsabilidad única es recibir una solicitud de notificación genérica,
 * identificar qué tipo de canal se requiere (Email, SMS, etc.) y delegar
 * la ejecución a la estrategia concreta correspondiente.
 * Este diseño permite que el Listener de Kafka no necesite conocer los detalles
 * de implementación de cada canal de comunicación, manteniendo el código desacoplado.
 */
@Service
@Slf4j
public class NotificationOrchestratorService {

    /**
     * Mapa que asocia cada tipo de notificación con su implementación de estrategia.
     * Se utiliza como caché para una búsqueda rápida (O(1)) de la estrategia a ejecutar.
     */
    private final Map<NotificationType, NotificationStrategy> strategies;

    /**
     * Constructor para la inyección de dependencias dinámica.
     * Spring Boot detecta automáticamente todas las clases que implementan la interfaz
     * NotificationStrategy y las inyecta como una lista.
     * El constructor itera sobre esta lista y puebla el mapa 'strategies', usando
     * el tipo de notificación (getTipo()) como clave. Esto significa que para agregar
     * un nuevo canal (ej. SMS), solo basta con crear la clase Strategy correspondiente
     * y Spring la registrará automáticamente aquí sin modificar este código (Open/Closed Principle).
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
     * Procesa una solicitud de notificación entrante.
     * 1. Valida que la solicitud sea correcta.
     * 2. Busca la estrategia adecuada en el mapa basándose en el tipo de notificación.
     * 3. Delega el envío a la estrategia encontrada.
     */
    public void procesarNotificacion(NotificationRequestDTO request) {
        // Validaciones Básicas
        if (request == null || request.getTipo() == null) {
            log.warn("Solicitud de notificación inválida o sin tipo.");
            return;
        }

        // Selección de Estrategia
        NotificationStrategy strategy = strategies.get(request.getTipo());

        if (strategy == null) {
            log.error("No se encontró una estrategia para el tipo: {}", request.getTipo());
            // Aquí podríamos lanzar una excepción o enviar a una Dead Letter Queue (DLQ)
            return;
        }

        // Ejecución Delegada
        log.debug("Delegando al notificador: {}", request.getTipo());
        strategy.enviar(request);
    }
}