package co.cue.auth.models.kafka;

/**
 * Este enum debe ser idéntico al del notification-service.
 * Define las "claves" para que el consumidor pueda seleccionar la estrategia.
 */
public enum NotificationType {
    EMAIL,
    SMS,
    IN_APP_PUSH
}
