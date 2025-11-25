package co.cue.auth.services;

import co.cue.auth.models.kafka.NotificationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio de infraestructura encargado de publicar mensajes en el bus de eventos (Kafka).
 * Actúa como el Productor en el patrón Publish-Subscribe. Su responsabilidad es tomar
 * los eventos de dominio o solicitudes de notificación generados por la lógica de negocio
 * y enviarlos a los tópicos correspondientes para que sean procesados asíncronamente
 * por otros microservicios (Consumidores).
 * Utiliza KafkaTemplate, una abstracción de alto nivel de Spring para el envío de mensajes.
 */
@Service
@Slf4j
@AllArgsConstructor
public class KafkaProducerService {

    /**
     * Plantilla de Spring Kafka para el envío de mensajes.
     * Configurada para serializar las claves como String y los valores como JSON (Object).
     */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Nombre del tópico destinado a los eventos de registro de usuarios.
     * Este nombre debe coincidir exactamente con el configurado en el listener del notification-service.
     */
    public static final String USUARIOS_REGISTRADOS_TOPIC = "usuarios_registrados_topic";

    /**
     * Publica una solicitud de notificación en el tópico de Kafka.
     * Este método implementa una comunicación "Fire-and-Forget" (Dispara y Olvida):
     * El servicio de autenticación envía el mensaje y continúa su ejecución inmediatamente,
     * sin esperar a que el correo sea enviado realmente. Esto mejora drásticamente la
     * latencia percibida por el usuario final durante el registro.
     */
    public void enviarNotificacion(NotificationRequestDTO request) {
        try {
            log.info("Enviando solicitud de notificación genérica tipo: {}", request.getTipo());

            // --- Envío Asíncrono ---
            // Enviamos el objeto al tópico. Spring Boot y la librería Jackson se encargan
            // automáticamente de serializar el objeto 'request' a formato JSON.
            kafkaTemplate.send(USUARIOS_REGISTRADOS_TOPIC, request);

        } catch (Exception e) {
            // Manejo de Fallos (Resiliencia)
            // Si Kafka no está disponible, capturamos la excepción para evitar que
            // la transacción principal (el registro del usuario) falle.
            // (Nota de Arquitectura): En un entorno productivo ideal, aquí deberíamos guardar
            // el evento en una tabla de "Outbox" para reintentarlo luego (Patrón Transactional Outbox),
            // pero por ahora solo logueamos el error (Best Effort).
            log.error("Error al enviar NotificationRequestDTO a Kafka", e);
        }
    }
}