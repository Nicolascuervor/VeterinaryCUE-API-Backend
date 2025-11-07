package co.cue.carrito_service.services;

import co.cue.carrito_service.client.InventarioServiceClient;
import co.cue.carrito_service.mapper.CarritoMapper;
import co.cue.carrito_service.models.dtos.requestdtos.AddItemRequestDTO;
import co.cue.carrito_service.models.dtos.responsedtos.CarritoResponseDTO;
import co.cue.carrito_service.models.entities.Carrito;
import co.cue.carrito_service.models.entities.ItemCarrito;
import co.cue.carrito_service.repository.CarritoRepository;
import co.cue.carrito_service.repository.ItemCarritoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CarritoServiceImpl implements ICarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final CarritoMapper carritoMapper;
    private final InventarioServiceClient inventarioServiceClient;

    @Override
    @Transactional(readOnly = true)
    public CarritoResponseDTO getOrCreateCarrito(Long usuarioId, String sessionId) {
        Carrito carrito = findOrCreateCarrito(usuarioId, sessionId);
        return carritoMapper.toCarritoResponseDTO(carrito);
    }

    @Override
    @Transactional
    public CarritoResponseDTO addItem(Long usuarioId, String sessionId, AddItemRequestDTO itemDTO) {
        // 1. Validar que el producto existe
        inventarioServiceClient.findProductoById(itemDTO.getProductoId());

        // 2. Obtener el carrito
        Carrito carrito = findOrCreateCarrito(usuarioId, sessionId);

        // 3. Buscar si el item ya existe en el carrito
        Optional<ItemCarrito> itemExistenteOpt = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(itemDTO.getProductoId()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            // Si existe, actualiza la cantidad
            ItemCarrito itemExistente = itemExistenteOpt.get();
            itemExistente.setCantidad(itemExistente.getCantidad() + itemDTO.getCantidad());
            itemCarritoRepository.save(itemExistente); // Guarda el item actualizado
        } else {
            // Si no existe, crea un nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setProductoId(itemDTO.getProductoId());
            nuevoItem.setCantidad(itemDTO.getCantidad());
            nuevoItem.setCarrito(carrito); // Asocia con el carrito padre
            carrito.getItems().add(nuevoItem); // Añade a la colección
        }

        // 4. Guardar el carrito
        Carrito carritoGuardado = carritoRepository.save(carrito);
        return carritoMapper.toCarritoResponseDTO(carritoGuardado);
    }

    @Override
    @Transactional
    public CarritoResponseDTO removeItem(Long usuarioId, String sessionId, Long productoId) {
        Carrito carrito = findCarrito(usuarioId, sessionId);

        // Se usa removeIf para eliminar el item de la colección
        boolean removed = carrito.getItems().removeIf(
                item -> item.getProductoId().equals(productoId)
        );

        if (!removed) {
            throw new EntityNotFoundException("Item con productoId " + productoId + " no encontrado en el carrito.");
        }

        Carrito carritoGuardado = carritoRepository.save(carrito);
        return carritoMapper.toCarritoResponseDTO(carritoGuardado);
    }

    @Override
    @Transactional
    public void clearCarrito(Long usuarioId, String sessionId) {
        Carrito carrito = findCarrito(usuarioId, sessionId);
        carrito.getItems().clear(); // Limpia la colección
        carritoRepository.save(carrito); // orphanRemoval=true borra los items
    }




    /**
     * Busca un carrito. Si no lo encuentra, lanza Excepción.
     */
    private Carrito findCarrito(Long usuarioId, String sessionId) {
        if (usuarioId != null) {
            return carritoRepository.findByUsuarioId(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado para usuarioId: " + usuarioId));
        }
        if (sessionId != null) {
            return carritoRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado para sessionId: " + sessionId));
        }
        throw new IllegalArgumentException("Se requiere un usuarioId o sessionId.");
    }

    /**
     * Busca un carrito. Si no lo encuentra, LO CREA.
     */
    private Carrito findOrCreateCarrito(Long usuarioId, String sessionId) {
        if (usuarioId != null) {
            // Busca por Usuario. Si no existe, crea uno nuevo para ese usuario.
            return carritoRepository.findByUsuarioId(usuarioId)
                    .orElseGet(() -> createNuevoCarrito(usuarioId, null));
        }
        if (sessionId != null) {
            // Busca por Sesión. Si no existe, crea uno nuevo para esa sesión.
            return carritoRepository.findBySessionId(sessionId)
                    .orElseGet(() -> createNuevoCarrito(null, sessionId));
        }
        //  Si el frontend no manda ni usuario ni sesión, es un error.
        throw new IllegalArgumentException("Se requiere un usuarioId o sessionId para obtener o crear un carrito.");
    }

    private Carrito createNuevoCarrito(Long usuarioId, String sessionId) {
        Carrito nuevoCarrito = new Carrito();
        if (usuarioId != null) {
            nuevoCarrito.setUsuarioId(usuarioId);
        } else {
            nuevoCarrito.setSessionId(sessionId);
        }
        return carritoRepository.save(nuevoCarrito);
    }
}
