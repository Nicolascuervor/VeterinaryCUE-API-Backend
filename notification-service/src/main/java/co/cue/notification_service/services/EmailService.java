package co.cue.notification_service.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoBienvenida(String nombre, String correo) {
        log.info("Intentando enviar correo de bienvenida a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@veterinariacue.com");
            message.setTo(correo);
            message.setSubject("¡Bienvenido a Veterinaria CUE!");
            message.setText("Hola " + nombre + ",\n\nTu cuenta ha sido creada exitosamente. " +
                    "¡Estamos contentos de tenerte con nosotros!");

            mailSender.send(message);
            log.info("Correo de bienvenida enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error al enviar correo de bienvenida: {}", e.getMessage());
        }
    }

    public void enviarResumenFactura(String correo, String nombre, String numFactura, String total, String fecha) {
        log.info("Preparando correo de factura para {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@veterinariacue.com");
            message.setTo(correo);
            message.setSubject("Comprobante de Compra - " + numFactura);
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
