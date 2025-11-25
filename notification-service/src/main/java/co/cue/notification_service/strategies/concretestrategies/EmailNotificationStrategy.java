package co.cue.notification_service.strategies.concretestrategies;

import co.cue.notification_service.models.dtos.requestdtos.NotificationRequestDTO;
import co.cue.notification_service.models.enums.NotificationType;
import co.cue.notification_service.services.EmailService;
import co.cue.notification_service.strategies.NotificationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el envío de correos electrónicos estándar.
 * Esta clase implementa la lógica necesaria para procesar solicitudes de notificación
 * de tipo EMAIL. Se utiliza principalmente para comunicaciones transaccionales simples,
 * como el correo de bienvenida tras el registro de un usuario.
 * Al estar anotada con @Component, Spring la detecta automáticamente y el
 * NotificationOrchestratorService la registra en su mapa de estrategias disponibles.
 */
@Component
@AllArgsConstructor
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    /**
     * Ejecuta el envío del correo electrónico utilizando los datos del payload.
     * Realiza las siguientes acciones:
     * 1. Extrae los datos necesarios (nombre, correo) del mapa genérico.
     * 2. Valida que la información requerida esté presente. Si falta algún dato crítico,
     * aborta la operación para evitar errores en el servicio de correo.
     * 3. Delega el envío real al servicio de infraestructura (EmailService).
     */
    @Override
    public void enviar(NotificationRequestDTO request) {
        log.info("Ejecutando Estrategia EMAIL...");
        try {
            // Extracción y Validación de Datos
            // El DTO es genérico, por lo que debemos castear o extraer manualmente
            // las claves que esta estrategia específica espera.
            String nombre = request.getPayload().get("nombre");
            String correo = request.getPayload().get("correo");

            if (nombre == null || correo == null) {
                log.warn("Payload incompleto para Estrategia EMAIL. Faltan 'nombre' o 'correo'.");
                return;
            }

            // Delegación al Servicio de Infraestructura
            emailService.enviarCorreoBienvenida(nombre, correo);

        } catch (Exception e) {
            log.error("Error al procesar estrategia EMAIL: {}", e.getMessage(), e);
        }
    }

    /**
     * Retorna el identificador del tipo de notificación que esta estrategia maneja.
     * Devuelve NotificationType.EMAIL, lo que permite al orquestador enrutar
     * los mensajes de este tipo hacia esta clase específica.
     */
    @Override
    public NotificationType getTipo() {
        return NotificationType.EMAIL;
    }
}