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

    private static final String NO_EMAIL = "no-reply@veterinariacue.com";
    private static final String SALUDO = "Hola ";


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
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("¡Bienvenido a Veterinaria CUE!");
            message.setText(SALUDO + nombre + ",\n\nTu cuenta ha sido creada exitosamente. " +
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
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Comprobante de Compra - " + numFactura);

            // Construcción manual de la plantilla de texto.
            // En producción, esto se reemplazaría por un motor de plantillas como Thymeleaf o FreeMarker.
            message.setText(SALUDO + nombre + ",\n\n" +
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

    public void enviarConfirmacionCita(String correo, String nombreDuenio, String nombreMascota, String fecha, String medico) {
        log.info("Enviando confirmación de cita a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Confirmación de Cita - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Tu cita ha sido confirmada exitosamente.\n\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Veterinario: " + medico + "\n" +
                    "Fecha y Hora: " + fecha + "\n\n" +
                    "Por favor llega 10 minutos antes.");
            
            mailSender.send(message);
            log.info("Correo de cita enviado.");
        } catch (Exception e) {
            log.error("Error enviando correo de cita: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo al cliente cuando se crea una nueva cita, incluyendo un link para confirmarla.
     * El cliente debe hacer clic en el link para confirmar la cita.
     */
    public void enviarConfirmacionCitaConLink(String correo, String nombreDuenio, String nombreMascota, String fecha, String medico, String linkConfirmacion) {
        log.info("Enviando confirmación de cita con link a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("¡Confirma tu Cita - Veterinaria CUE");
            
            // Construir la URL completa del link de confirmación (URL del frontend)
            String urlConfirmacion = "https://veterinariacue.com/confirmar-cita/" + linkConfirmacion;
            
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Se ha agendado una nueva cita para tu mascota.\n\n" +
                    "Detalles de la cita:\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Veterinario: " + medico + "\n" +
                    "Fecha y Hora: " + fecha + "\n\n" +
                    "⚠️ IMPORTANTE: Para confirmar tu cita, por favor haz clic en el siguiente enlace:\n" +
                    urlConfirmacion + "\n\n" +
                    "Tu cita quedará confirmada una vez que hagas clic en el enlace.\n" +
                    "Si no confirmas la cita, esta podría ser cancelada.\n\n" +
                    "¡Esperamos verte pronto!");
            
            mailSender.send(message);
            log.info("Correo de confirmación de cita con link enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de confirmación de cita con link: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo al cliente cuando se registra una nueva mascota en el sistema.
     */
    public void enviarNotificacionMascotaCreada(String correo, String nombreDuenio, String nombreMascota, String especie, String raza) {
        log.info("Enviando notificación de mascota creada a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Nueva Mascota Registrada - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "¡Felicidades! Tu mascota ha sido registrada exitosamente en nuestro sistema.\n\n" +
                    "Detalles de tu mascota:\n" +
                    "Nombre: " + nombreMascota + "\n" +
                    "Especie: " + especie + "\n" +
                    (raza != null && !raza.isEmpty() ? "Raza: " + raza + "\n" : "") +
                    "\n" +
                    "Ahora puedes agendar citas para " + nombreMascota + " desde tu cuenta.\n\n" +
                    "¡Gracias por confiar en nosotros!");
            
            mailSender.send(message);
            log.info("Correo de mascota creada enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de mascota creada: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo cuando una cita es cancelada.
     */
    public void enviarNotificacionCitaCancelada(String correo, String nombreDuenio, String nombreMascota, String fecha, String motivo) {
        log.info("Enviando notificación de cita cancelada a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Cita Cancelada - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Te informamos que tu cita ha sido cancelada.\n\n" +
                    "Detalles de la cita cancelada:\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Fecha y Hora: " + fecha + "\n" +
                    (motivo != null && !motivo.isEmpty() ? "Motivo: " + motivo + "\n" : "") +
                    "\n" +
                    "Si deseas reagendar, puedes hacerlo desde tu cuenta o contactándonos.\n\n" +
                    "Lamentamos cualquier inconveniente.");
            
            mailSender.send(message);
            log.info("Correo de cita cancelada enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de cita cancelada: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo cuando una cita comienza (estado EN_PROGRESO).
     */
    public void enviarNotificacionCitaEnProgreso(String correo, String nombreDuenio, String nombreMascota, String fecha, String medico) {
        log.info("Enviando notificación de cita en progreso a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Cita en Progreso - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Tu cita ha comenzado.\n\n" +
                    "Detalles:\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Veterinario: " + medico + "\n" +
                    "Fecha y Hora: " + fecha + "\n\n" +
                    "Tu mascota está siendo atendida. Te mantendremos informado sobre el progreso.");
            
            mailSender.send(message);
            log.info("Correo de cita en progreso enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de cita en progreso: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo cuando una cita es finalizada.
     */
    public void enviarNotificacionCitaFinalizada(String correo, String nombreDuenio, String nombreMascota, String fecha, String medico) {
        log.info("Enviando notificación de cita finalizada a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Cita Finalizada - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Tu cita ha sido finalizada exitosamente.\n\n" +
                    "Detalles:\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Veterinario: " + medico + "\n" +
                    "Fecha y Hora: " + fecha + "\n\n" +
                    "El historial clínico de " + nombreMascota + " ha sido actualizado. " +
                    "Puedes revisarlo desde tu cuenta.\n\n" +
                    "¡Gracias por confiar en nosotros!");
            
            mailSender.send(message);
            log.info("Correo de cita finalizada enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de cita finalizada: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo cuando un paciente no asistió a la cita.
     */
    public void enviarNotificacionCitaNoAsistio(String correo, String nombreDuenio, String nombreMascota, String fecha) {
        log.info("Enviando notificación de no asistencia a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Cita No Asistida - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Te informamos que se registró que no asististe a tu cita programada.\n\n" +
                    "Detalles de la cita:\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Fecha y Hora: " + fecha + "\n\n" +
                    "Si deseas reagendar, puedes hacerlo desde tu cuenta o contactándonos.\n\n" +
                    "Recuerda que es importante avisar con anticipación si no puedes asistir.");
            
            mailSender.send(message);
            log.info("Correo de no asistencia enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de no asistencia: {}", e.getMessage());
        }
    }

    public void enviarConfirmacionCitaVeterinario(String correo, String nombreVeterinario, String nombreDuenio, String nombreMascota, String fecha) {
        log.info("Enviando confirmación de cita al veterinario {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Nueva Cita Agendada - Veterinaria CUE");
            message.setText(SALUDO + nombreVeterinario + ",\n\n" +
                    "Se ha agendado una nueva cita para tu atención.\n\n" +
                    "Detalles de la cita:\n" +
                    "Cliente: " + nombreDuenio + "\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Fecha y Hora: " + fecha + "\n\n" +
                    "Por favor asegúrate de estar disponible en el horario indicado.");

            mailSender.send(message);
            log.info("Correo de cita enviado al veterinario.");
        } catch (Exception e) {
            log.error("Error enviando correo de cita al veterinario: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo cuando se crea un nuevo historial clínico para una mascota.
     */
    public void enviarNotificacionHistorialClinicoCreado(String correo, String nombreDuenio, String nombreMascota, String fecha, String diagnostico) {
        log.info("Enviando notificación de historial clínico creado a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Nuevo Historial Clínico - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Te informamos que se ha creado un nuevo historial clínico para tu mascota.\n\n" +
                    "Detalles:\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Fecha: " + fecha + "\n" +
                    "Diagnóstico: " + (diagnostico != null && !diagnostico.isEmpty() ? diagnostico : "Sin diagnóstico registrado") + "\n\n" +
                    "Puedes revisar el historial clínico completo desde tu cuenta.\n\n" +
                    "¡Gracias por confiar en nosotros!");
            
            mailSender.send(message);
            log.info("Correo de historial clínico creado enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de historial clínico creado: {}", e.getMessage());
        }
    }

    /**
     * Envía un correo cuando se reasigna el horario de una cita.
     */
    public void enviarNotificacionCitaHorarioReasignado(String correo, String nombreDuenio, String nombreMascota, String fechaAnterior, String fechaNueva, String medico) {
        log.info("Enviando notificación de cambio de horario de cita a {}", correo);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_EMAIL);
            message.setTo(correo);
            message.setSubject("Cambio de Horario de Cita - Veterinaria CUE");
            message.setText(SALUDO + nombreDuenio + ",\n\n" +
                    "Te informamos que el horario de tu cita ha sido reasignado.\n\n" +
                    "Detalles de la cita:\n" +
                    "Mascota: " + nombreMascota + "\n" +
                    "Veterinario: " + medico + "\n" +
                    "Horario anterior: " + fechaAnterior + "\n" +
                    "Nuevo horario: " + fechaNueva + "\n\n" +
                    "Por favor ten en cuenta este cambio y llega 10 minutos antes del nuevo horario.\n\n" +
                    "Si tienes alguna pregunta o necesitas realizar otro cambio, no dudes en contactarnos.\n\n" +
                    "Lamentamos cualquier inconveniente.");
            
            mailSender.send(message);
            log.info("Correo de cambio de horario enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error enviando correo de cambio de horario: {}", e.getMessage());
        }
    }
}