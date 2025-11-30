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
    @Transactional
    // Obtiene o crea un carrito para un usuario o sesión
    public CarritoResponseDTO getOrCreateCarrito(Long usuarioId, String sessionId) {
        Carrito carrito = findOrCreateCarrito(usuarioId, sessionId);
        return carritoMapper.toCarritoResponseDTO(carrito);
    }

    @Override
    @Transactional
    // Añade un item al carrito, si ya existe incrementa la cantidad
    public CarritoResponseDTO addItem(Long usuarioId, String sessionId, AddItemRequestDTO itemDTO) {
        // Valida que el producto exista en inventario
        inventarioServiceClient.findProductoById(itemDTO.getProductoId());
        Carrito carrito = findOrCreateCarrito(usuarioId, sessionId);
        Optional<ItemCarrito> itemExistenteOpt = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(itemDTO.getProductoId()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            // Si el item ya está, incrementa la cantidad
            ItemCarrito itemExistente = itemExistenteOpt.get();
            itemExistente.setCantidad(itemExistente.getCantidad() + itemDTO.getCantidad());
            itemCarritoRepository.save(itemExistente);
        } else {
            // Si no está, crea un nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setProductoId(itemDTO.getProductoId());
            nuevoItem.setCantidad(itemDTO.getCantidad());
            nuevoItem.setCarrito(carrito);
            carrito.getItems().add(nuevoItem);
        }
        Carrito carritoGuardado = carritoRepository.save(carrito);
        return carritoMapper.toCarritoResponseDTO(carritoGuardado);
    }

    @Override
    @Transactional
    // Remueve un item del carrito por productoId
    public CarritoResponseDTO removeItem(Long usuarioId, String sessionId, Long productoId) {
        Carrito carrito = findCarrito(usuarioId, sessionId);
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
    // Vacía todos los items del carrito
    public void clearCarrito(Long usuarioId, String sessionId) {
        Carrito carrito = findCarrito(usuarioId, sessionId);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }

    // Busca un carrito existente por usuario o sesión
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

    // Busca un carrito o crea uno nuevo si no existe
    private Carrito findOrCreateCarrito(Long usuarioId, String sessionId) {
        if (usuarioId != null) {
            // Busca por Usuario. Si no existe, crea uno nuevo para ese usuario.
            return carritoRepository.findByUsuarioId(usuarioId)
                    .orElseGet(() -> createNuevoCarrito(usuarioId, null));
        }
        if (sessionId != null) {
            return carritoRepository.findBySessionId(sessionId)
                    .orElseGet(() -> createNuevoCarrito(null, sessionId));
        }
        throw new IllegalArgumentException("Se requiere un usuarioId o sessionId para obtener o crear un carrito.");
    }
    // Crea un carrito nuevo
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
