package co.cue.carrito_service.models.dtos.responsedtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter

// DTO de respuesta para un carrito de compras
public class CarritoResponseDTO {

    // ID del carrito
    private Long id;

    // ID del usuario propietario (si existe)
    private Long usuarioId;

    // ID de la sesión si el carrito es anónimo
    private String sessionId;


    // Fecha de última actualización del carrito
    private LocalDateTime updatedAt;

    // Conjunto de items dentro del carrito
    private Set<ItemResponseDTO> items;
}