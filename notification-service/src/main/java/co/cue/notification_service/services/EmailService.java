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
            message.setFrom("no-reply@veterinariacue.com"); // (O tu correo de gmail)
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
}
