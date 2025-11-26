package co.cue.historias_clinicas_service.client;

import lombok.Data;
import lombok.NoArgsConstructor;

// Clase DTO utilizada para recibir datos básicos de la mascota
// obtenidos desde el microservicio de mascotas.

@Data

// Genera un constructor vacío necesario para la deserialización.
@NoArgsConstructor
public class MascotaClienteDTO {

    // Identificador del dueño de la mascota.
    private Long duenioId;
}
