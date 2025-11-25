package co.cue.auth.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Evento de dominio que representa el registro exitoso de un usuario.
 * Este POJO (Plain Old Java Object) actúa como un DTO (Data Transfer Object) para la mensajería asíncrona.
 * Su propósito es encapsular los datos mínimos necesarios que otros microservicios (como el de Notificaciones)
 * necesitan conocer cuando ocurre el evento de "Registro".
 * Al ser un evento, representa algo que ya ocurrió en el pasado, por lo que su estructura
 * suele ser simple y directa para facilitar la serialización a JSON (para Kafka).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistradoEvent {
    // Datos del Evento
    // Definimos los atributos payload que viajarán en el mensaje de Kafka.
    // Solo incluimos 'nombre' y 'correo' porque son los únicos datos necesarios
    // para enviar el correo de bienvenida. No enviamos contraseñas ni datos sensibles.
    private String nombre;
    private String correo;
}
