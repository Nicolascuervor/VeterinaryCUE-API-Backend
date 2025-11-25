package co.cue.notification_service.listener;


import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.services.NotificationOrchestratorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de escuchar y consumir mensajes desde Apache Kafka.
 * Actúa como el adaptador de entrada para la comunicación asíncrona.
 * Su responsabilidad es suscribirse a los tópicos configurados, deserializar
 * los mensajes entrantes (JSON a Objetos Java) y delegar su procesamiento
 * a la capa de servicio correspondiente.
 * Este diseño desacopla el mecanismo de transporte (Kafka) de la lógica de negocio
 * (envío de emails, SMS, etc.), facilitando las pruebas y el mantenimiento.
 */
@Component
@Slf4j
@AllArgsConstructor
public class KafkaConsumerListener {
    /**
     * Servicio orquestador que decide qué estrategia de notificación ejecutar.
     */
    private final NotificationOrchestratorService orchestratorService;

    /**
     * Método oyente (Listener) para eventos de registro de usuarios.
     *
     * La anotación @KafkaListener instruye a Spring para que cree un consumidor en segundo plano
     * que se mantenga conectado al broker.
     *
     * Configuración clave:
     * - topics: "usuarios_registrados_topic" -> Debe coincidir con el tópico donde publica el Auth Service.
     * - groupId: "notificaciones_bienvenida_group" -> Identifica a este grupo de consumidores.
     * Si escalamos este microservicio a múltiples instancias, Kafka usará este ID para balancear
     * los mensajes entre ellas (cada mensaje se entrega a una sola instancia del grupo).
     */
    @KafkaListener(topics = "usuarios_registrados_topic",
            groupId = "notificaciones_bienvenida_group")
    public void handleNotificationRequest(NotificationRequestDTO event) {
        log.info("Evento de notificación recibido para tipo: {}", event.getTipo());

        // Delegamos al orquestador para que seleccione la estrategia (Email, SMS)
        // y ejecute el envío real.
        orchestratorService.procesarNotificacion(event);
    }
}