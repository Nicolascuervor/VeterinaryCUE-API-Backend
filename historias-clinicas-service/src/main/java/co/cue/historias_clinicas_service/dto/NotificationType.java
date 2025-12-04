package co.cue.historias_clinicas_service.dto;

/**
 * Enum local para tipos de notificación.
 * Debe coincidir con el enum del notification-service.
 */
public enum NotificationType {
    EMAIL,
    SMS,
    IN_APP_PUSH,
    FACTURA,
    CITA_CONFIRMACION,
    MASCOTA_CREADA,
    CITA_CANCELADA,
    CITA_EN_PROGRESO,
    CITA_FINALIZADA,
    CITA_NO_ASISTIO,
    HISTORIAL_CLINICO_CREADO
}

