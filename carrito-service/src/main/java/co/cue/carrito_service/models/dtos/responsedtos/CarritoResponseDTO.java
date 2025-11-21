package co.cue.carrito_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CarritoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String sessionId;
    private LocalDateTime updatedAt;
    private Set<ItemResponseDTO> items;
}