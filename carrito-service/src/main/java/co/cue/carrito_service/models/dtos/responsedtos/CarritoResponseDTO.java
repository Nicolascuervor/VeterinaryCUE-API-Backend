package co.cue.carrito_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

// La respuesta completa del carrito.
@Getter
@Setter
public class CarritoResponseDTO {
    private Long id;
    private Long usuarioId; // Será nulo si es un invitado
    private String sessionId; // Será nulo si es un usuario registrado
    private LocalDateTime updatedAt;
    private Set<ItemResponseDTO> items;
}