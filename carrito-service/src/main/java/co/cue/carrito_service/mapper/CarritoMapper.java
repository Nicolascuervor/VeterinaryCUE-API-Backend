package co.cue.carrito_service.mapper;

import co.cue.carrito_service.models.dtos.responsedtos.CarritoResponseDTO;
import co.cue.carrito_service.models.dtos.responsedtos.ItemResponseDTO;
import co.cue.carrito_service.models.entities.Carrito;
import co.cue.carrito_service.models.entities.ItemCarrito;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
// Mapper para convertir entidades de carrito e items a sus respectivos DTOs
public class CarritoMapper {

    /**
     * Convierte la entidad Carrito a su DTO de respuesta.
     */
    public CarritoResponseDTO toCarritoResponseDTO(Carrito carrito) {
        CarritoResponseDTO dto = new CarritoResponseDTO();
        dto.setId(carrito.getId());
        dto.setUsuarioId(carrito.getUsuarioId());
        dto.setSessionId(carrito.getSessionId());
        dto.setUpdatedAt(carrito.getUpdatedAt());

        // Convierte todos los items del carrito a sus DTOs
        dto.setItems(
                carrito.getItems().stream()
                        .map(this::toItemResponseDTO)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    /**
     * Convierte la entidad ItemCarrito a su DTO de respuesta.
     */
    public ItemResponseDTO toItemResponseDTO(ItemCarrito item) {
        return new ItemResponseDTO(
                item.getProductoId(),
                item.getCantidad()
        );
    }
}