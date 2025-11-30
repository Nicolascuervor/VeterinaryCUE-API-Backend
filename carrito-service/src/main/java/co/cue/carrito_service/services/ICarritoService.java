package co.cue.carrito_service.services;

import co.cue.carrito_service.models.dtos.requestdtos.AddItemRequestDTO;
import co.cue.carrito_service.models.dtos.responsedtos.CarritoResponseDTO;

public interface ICarritoService {

    // Obtiene un carrito existente para un usuario o sesión. Si no existe, lo crea.
    CarritoResponseDTO getOrCreateCarrito(Long usuarioId, String sessionId);

    // Añade un producto al carrito. Si el producto ya existe, suma la cantidad.
    CarritoResponseDTO addItem(Long usuarioId, String sessionId, AddItemRequestDTO itemDTO);

    // Remueve un producto del carrito según su ID.
    CarritoResponseDTO removeItem(Long usuarioId, String sessionId, Long productoId);

    // Vacía todos los productos del carrito.
    void clearCarrito(Long usuarioId, String sessionId);
}
