package co.cue.notification_service.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Evento de dominio que representa el registro de un nuevo usuario (Vista del Consumidor).
 * Esta clase es un espejo del evento emitido por el servicio de autenticación.
 * Actúa como el contrato de datos (DTO) que el consumidor de Kafka en este microservicio
 * espera recibir y deserializar.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistradoEvent {

    // Datos del Evento
    // Estos campos deben coincidir en nombre y tipo con el JSON enviado por el productor
    // para que la deserialización automática de Jackson/Kafka funcione correctamente.

    private String nombre;
    private String correo;
}