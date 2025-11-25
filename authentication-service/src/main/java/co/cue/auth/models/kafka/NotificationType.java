package co.cue.auth.models.kafka;

/**
 * Enumeración que define los canales de comunicación disponibles para las notificaciones.
 *
 * Este enum actúa como un contrato compartido entre los microservicios.
 * El servicio de autenticación (Productor) utiliza estos valores para indicarle
 * al servicio de notificaciones (Consumidor) por qué medio debe contactar al usuario.
 *
 * Es fundamental que los valores definidos aquí coincidan con los que espera
 * la lógica de enrutamiento (Strategy Pattern) en el consumidor.
 */
public enum NotificationType {

    /**
     * Indica que la notificación debe enviarse por correo electrónico.
     * Requiere que el payload del mensaje contenga al menos un campo "correo".
     */
    EMAIL,

    /**
     * Indica que la notificación debe enviarse por mensaje de texto (SMS).
     * Requiere que el payload contenga un número de teléfono válido.
     * (Preparado para futura implementación).
     */
    SMS,

    /**
     * Indica que la notificación debe enviarse como una alerta Push en la aplicación móvil o web.
     * Requiere token de dispositivo o ID de usuario.
     * (Preparado para futura implementación).
     */
    IN_APP_PUSH
}
