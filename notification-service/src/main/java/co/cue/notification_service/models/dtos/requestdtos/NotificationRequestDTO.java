package co.cue.notification_service.models.dtos.requestdtos;

import co.cue.notification_service.models.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 *    DTO genérico para la cola de Kafka.
 *
 * - tipo: Le dice a nuestro servicio qué estrategia usar (EMAIL, SMS, etc.).
 * - payload: Un mapa flexible que contiene los datos que CADA estrategia necesita.
 * Para EMAIL, contendrá "nombre" y "correo".
 * Para SMS, podría contener "telefono" y "codigo".
 */
/**
 * Objeto de Transferencia de Datos (DTO) para recibir solicitudes de notificación.
 * Esta clase actúa como el contrato de datos esperado por el consumidor de Kafka
 * en este microservicio. Representa la estructura del mensaje JSON que llega
 * desde los productores (como auth-service o pedidos-service).
 * Diseño:
 * Al igual que en el productor, mantenemos esta clase desacoplada (copia local)
 * para que el servicio de notificaciones sea autónomo y no dependa de librerías externas.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    /**
     * El tipo de notificación solicitada (Discriminador de Estrategia).
     * Este campo es fundamental para el patrón Strategy implementado en el orquestador.
     * Indica si el sistema debe invocar el servicio de Email, SMS, Push, etc.
     * Debe coincidir con uno de los valores del enum local NotificationType.
     */
    private NotificationType tipo;

    /**
     * Contenedor de datos dinámicos para la notificación.
     * Un mapa flexible que transporta los parámetros variables requeridos por cada
     * tipo de plantilla.
     * Ejemplos de contenido esperado según el tipo:
     * - EMAIL: { "correo": "x@y.com", "nombre": "Juan", "asunto": "..." }
     * - SMS: { "telefono": "+57300...", "mensaje": "..." }
     * El consumidor es responsable de extraer y validar las claves que necesita
     * de este mapa.
     */
    private Map<String, String> payload;
}
