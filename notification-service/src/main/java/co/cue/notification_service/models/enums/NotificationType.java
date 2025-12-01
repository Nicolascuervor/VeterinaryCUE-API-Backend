package co.cue.notification_service.models.enums;

/**
 * Enumeración que define los canales y tipos de notificaciones soportados por el sistema.
 * En el contexto del Consumidor (notification-service), este enum cumple una función crítica:
 * Actúa como la clave para el patrón Strategy en el NotificationOrchestratorService.
 * Cada valor aquí definido debe tener una implementación correspondiente de la interfaz
 * NotificationStrategy. Si llega un mensaje con un tipo no mapeado, el sistema no sabrá
 * cómo procesarlo.
 */
public enum NotificationType {

    /**
     * Estrategia para envío de correos electrónicos transaccionales.
     * Se utiliza para mensajes genéricos como bienvenidas, recuperación de contraseña, etc.
     * Requiere un payload con "correo", "nombre" y "asunto".
     */
    EMAIL,

    /**
     * Estrategia para envío de mensajes de texto (SMS).
     * Reservado para implementaciones futuras (ej. integración con Twilio).
     */
    SMS,

    /**
     * Estrategia para notificaciones push dentro de la aplicación móvil/web.
     * Reservado para implementaciones futuras (ej. Firebase Cloud Messaging).
     */
    IN_APP_PUSH,

    /**
     * Estrategia especializada para el envío de facturas y comprobantes de pago.
     * A diferencia del tipo EMAIL genérico, este tipo activa una lógica de negocio distinta
     * (FacturaEmailNotificationStrategy) que puede incluir formateo avanzado de tablas HTML
     * o adjuntar PDFs, requiriendo datos específicos como "total", "items" y "fecha".
     */
    FACTURA,

    CITA_CONFIRMACION


}
