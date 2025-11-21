package co.cue.carrito_service.services;

import co.cue.carrito_service.models.dtos.requestdtos.AddItemRequestDTO;
import co.cue.carrito_service.models.dtos.responsedtos.CarritoResponseDTO;

public interface ICarritoService {
    CarritoResponseDTO getOrCreateCarrito(Long usuarioId, String sessionId);
    CarritoResponseDTO addItem(Long usuarioId, String sessionId, AddItemRequestDTO itemDTO);
    CarritoResponseDTO removeItem(Long usuarioId, String sessionId, Long productoId);
    void clearCarrito(Long usuarioId, String sessionId);
}
