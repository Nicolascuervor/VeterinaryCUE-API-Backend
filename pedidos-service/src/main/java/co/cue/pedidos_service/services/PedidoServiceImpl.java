package co.cue.pedidos_service.services;

import co.cue.pedidos_service.client.AuthServiceClient;
import co.cue.pedidos_service.client.CarritoServiceClient;
import co.cue.pedidos_service.client.InventarioServiceClient;
import co.cue.pedidos_service.exceptions.CarritoVacioException;
import co.cue.pedidos_service.exceptions.StockInsuficienteException;
import co.cue.pedidos_service.models.dtos.client.CarritoClienteDTO;
import co.cue.pedidos_service.models.dtos.client.ItemCarritoClienteDTO;
import co.cue.pedidos_service.models.dtos.client.ProductoClienteDTO;
import co.cue.pedidos_service.models.dtos.client.UsuarioClienteDTO;
import co.cue.pedidos_service.models.dtos.requestdtos.CheckoutGuestRequestDTO;
import co.cue.pedidos_service.models.dtos.responsedtos.CheckoutResponseDTO;
import co.cue.pedidos_service.models.entities.Pedido;
import co.cue.pedidos_service.models.entities.PedidoItem;
import co.cue.pedidos_service.models.enums.PedidoEstado;
import co.cue.pedidos_service.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final AuthServiceClient authClient;
    private final CarritoServiceClient carritoClient;
    private final InventarioServiceClient inventarioClient;
    private final StripeService stripeService;


    @Override
    @Transactional

    public CheckoutResponseDTO iniciarCheckout(Long usuarioId, String sessionId, CheckoutGuestRequestDTO guestDTO) {

        UsuarioClienteDTO cliente = obtenerDatosCliente(usuarioId, guestDTO).block();

        if (cliente == null) {
            log.error("No se pudieron obtener los datos del cliente para el usuarioId: {}", usuarioId);
            throw new EntityNotFoundException("No se pudieron verificar los datos del cliente.");
        }

        CarritoClienteDTO carrito = carritoClient.findCarrito(usuarioId, sessionId).block();
        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException("No se puede procesar un pedido con un carrito vacío.");
        }

        Set<PedidoItem> itemsDelPedido = new HashSet<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (ItemCarritoClienteDTO itemCarrito : carrito.getItems()) {
            ProductoClienteDTO producto = inventarioClient.findProductoById(itemCarrito.getProductoId()).block();

            if (producto == null || !producto.isDisponibleParaVenta()) {
                throw new StockInsuficienteException("El producto " + itemCarrito.getProductoId() + " no está disponible.");
            }
            if (producto.getStockActual() < itemCarrito.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre() +
                        ". Solicitados: " + itemCarrito.getCantidad() +
                        ", Disponibles: " + producto.getStockActual());
            }

            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProductoId(producto.getId());
            pedidoItem.setCantidad(itemCarrito.getCantidad());
            pedidoItem.setPrecioUnitario(producto.getPrecio());

            BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(itemCarrito.getCantidad()));
            pedidoItem.setSubtotalLinea(subtotal);

            itemsDelPedido.add(pedidoItem);
            totalPedido = totalPedido.add(subtotal);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId); // Nulo si es invitado
        pedido.setClienteNombre(cliente.getNombre() + " " + cliente.getApellido());
        pedido.setClienteEmail(cliente.getCorreo());
        pedido.setTotalPedido(totalPedido);
        pedido.setEstado(PedidoEstado.PENDIENTE);

        for (PedidoItem item : itemsDelPedido) {
            item.setPedido(pedido);
        }
        pedido.setItems(itemsDelPedido);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        String clientSecret = stripeService.crearPaymentIntent(totalPedido, pedidoGuardado.getId());


        pedidoGuardado.setStripePaymentIntentId(clientSecret.split("_secret_")[0]); // (Truco para el mock)
        pedidoRepository.save(pedidoGuardado);

        return new CheckoutResponseDTO(pedidoGuardado.getId(), clientSecret);
    }

    /**
     * Helper para obtener los datos del cliente, sea de Auth-service o del DTO de invitado.
     */
    private Mono<UsuarioClienteDTO> obtenerDatosCliente(Long usuarioId, CheckoutGuestRequestDTO guestDTO) {
        if (usuarioId != null) {
            // Caso 1: Usuario Logueado
            return authClient.findUsuarioById(usuarioId);
        } else if (guestDTO != null && guestDTO.getClienteEmail() != null) {
            // Caso 2: Usuario Invitado
            UsuarioClienteDTO dto = new UsuarioClienteDTO();
            dto.setNombre(guestDTO.getClienteNombre());
            dto.setApellido(""); // Invitado no tiene apellido separado
            dto.setCorreo(guestDTO.getClienteEmail());
            return Mono.just(dto);
        } else {
            // Caso 3: Error
            return Mono.error(new IllegalArgumentException("Datos del cliente (usuarioId o guestDTO) son requeridos."));
        }
    }
}