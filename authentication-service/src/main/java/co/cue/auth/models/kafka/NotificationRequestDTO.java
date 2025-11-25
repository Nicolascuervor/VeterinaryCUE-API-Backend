package co.cue.auth.models.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


/**
 * Objeto de Transferencia de Datos (DTO) para eventos de notificación asíncronos.
 *
 * Este objeto actúa como el "mensaje" que se deposita en la cola de Kafka.
 * Su diseño es intencionalmente genérico para desacoplar al productor (Auth Service)
 * del consumidor (Notification Service).
 *
 * En lugar de tener clases específicas como EmailDTO o SmsDTO, utilizamos una estructura
 * flexible (Tipo + Payload) que permite transmitir cualquier tipo de notificación
 * sin modificar el contrato de mensajería.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    /**
     * El tipo de notificación que se debe enviar (ej. EMAIL, SMS, PUSH).
     *
     * Este enumerado actúa como el discriminador que le indica al consumidor
     * qué estrategia de notificación debe instanciar y ejecutar.
     */
    private NotificationType tipo;

    /**
     * Contenedor flexible de datos para el cuerpo del mensaje.
     *
     * Utilizamos un Map<String, String> para enviar pares clave-valor dinámicos.
     * Por ejemplo:
     * - Para EMAIL: { "correo": "usuario@test.com", "nombre": "Juan", "asunto": "Bienvenido" }
     * - Para SMS: { "telefono": "+573001234567", "codigo": "8842" }
     *
     * Ventaja: Permite agregar nuevos parámetros a las plantillas de notificación
     * sin necesidad de recompilar o modificar la estructura de las clases en ambos microservicios.
     */
    private Map<String, String> payload;
}
