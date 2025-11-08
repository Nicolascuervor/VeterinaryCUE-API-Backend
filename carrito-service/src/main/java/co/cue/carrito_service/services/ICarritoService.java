package co.cue.carrito_service.services;

import co.cue.carrito_service.models.dtos.requestdtos.AddItemRequestDTO;
import co.cue.carrito_service.models.dtos.responsedtos.CarritoResponseDTO;

public interface ICarritoService {

    /** Obtiene el carrito de un cliente (registrado o invitado). Si no existe, lo crea */
    CarritoResponseDTO getOrCreateCarrito(Long usuarioId, String sessionId);

    /**
     * Agrega un item al carrito. Si el item ya existe, actualiza la cantidad.
     */
    CarritoResponseDTO addItem(Long usuarioId, String sessionId, AddItemRequestDTO itemDTO);

    /**
     * Elimina un item del carrito basado en su productoId.
     */
    CarritoResponseDTO removeItem(Long usuarioId, String sessionId, Long productoId);

    /**
     * Vacía todos los items del carrito.
     */
    void clearCarrito(Long usuarioId, String sessionId);
}
