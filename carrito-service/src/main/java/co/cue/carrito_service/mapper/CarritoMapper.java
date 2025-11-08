package co.cue.carrito_service.mapper;

import co.cue.carrito_service.models.dtos.responsedtos.CarritoResponseDTO;
import co.cue.carrito_service.models.dtos.responsedtos.ItemResponseDTO;
import co.cue.carrito_service.models.entities.Carrito;
import co.cue.carrito_service.models.entities.ItemCarrito;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
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