package co.cue.notification_service.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio de infraestructura encargado del envío físico de correos electrónicos.
 * Esta clase actúa como un adaptador sobre la librería JavaMail de Spring.
 * Su única responsabilidad es construir el objeto de mensaje SMTP y transmitirlo
 * al servidor de correo configurado en application.properties.
 * Es invocada por las distintas estrategias (EmailNotificationStrategy, FacturaEmailNotificationStrategy)
 * para ejecutar la acción final de comunicación.
 */
@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

    /**
     * Cliente de correo inyectado por Spring Boot.
     * Contiene la configuración del servidor SMTP (host, puerto, credenciales).
     */
    private final JavaMailSender mailSender;

    /**
     * Construye y envía un correo de bienvenida estándar.
     * Se utiliza cuando un nuevo usuario se registra en la plataforma.
     * Nota de Diseño:
     * Actualmente utiliza SimpleMailMessage para enviar texto plano.
     * En una versión futura, esto debería evolucionar a MimeMessage para soportar HTML
     * y plantillas visuales más atractivas (Thymeleaf).
     */
    public void enviarCorreoBienvenida(String nombre, String correo) {
        log.info("Intentando enviar correo de bienvenida a {}", correo);
        try {
            // --- Construcción del Mensaje ---
            // Creamos un objeto simple de correo (sin adjuntos ni HTML complejo).
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@veterinariacue.com"); // Remitente (puede ser ficticio si el SMTP lo permite)
            message.setTo(correo);
            message.setSubject("¡Bienvenido a Veterinaria CUE!");
            message.setText("Hola " + nombre + ",\n\nTu cuenta ha sido creada exitosamente. " +
                    "¡Estamos contentos de tenerte con nosotros!");

            // --- Transmisión SMTP ---
            // Enviamos el mensaje a través de la red. Esta operación es bloqueante y puede tardar unos segundos.
            mailSender.send(message);
            log.info("Correo de bienvenida enviado exitosamente.");

        } catch (Exception e) {
            // --- Tolerancia a Fallos ---
            // Si el servidor de correo falla (timeout, credenciales), capturamos el error
            // para que no se propague una excepción no controlada.
            // En un entorno productivo, esto debería enviar el mensaje a una cola de "Dead Letter" (DLQ)
            // para reintentarlo más tarde.
            log.error("Error al enviar correo de bienvenida: {}", e.getMessage());
        }
    }

    /**
     * Construye y envía un comprobante de factura simplificado.
     * Genera un cuerpo de texto con los detalles financieros de la transacción.
     */
    public void enviarResumenFactura(String correo, String nombre, String numFactura, String total, String fecha) {
        log.info("Preparando correo de factura para {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@veterinariacue.com");
            message.setTo(correo);
            message.setSubject("Comprobante de Compra - " + numFactura);

            // Construcción manual de la plantilla de texto.
            // En producción, esto se reemplazaría por un motor de plantillas como Thymeleaf o FreeMarker.
            message.setText("Hola " + nombre + ",\n\n" +
                    "Gracias por tu compra en Veterinaria CUE.\n" +
                    "Aquí tienes el resumen de tu factura:\n\n" +
                    "-----------------------------------\n" +
                    "No. Factura: " + numFactura + "\n" +
                    "Fecha: " + fecha + "\n" +
                    "TOTAL PAGADO: $" + total + "\n" +
                    "-----------------------------------\n\n" +
                    "Esperamos verte pronto.");

            mailSender.send(message);
            log.info("Correo de factura enviado exitosamente a {}", correo);

        } catch (Exception e) {
            log.error("Error al enviar factura: {}", e.getMessage());
        }
    }
}