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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final AuthServiceClient authClient;
    private final CarritoServiceClient carritoClient;
    private final InventarioServiceClient inventarioClient;
    private final StripeService stripeService;


    @Override
    @Transactional
    public CheckoutResponseDTO iniciarCheckout(Long usuarioId, String sessionId, CheckoutGuestRequestDTO guestDTO) {


        // Paso 1: Obtener Datos del Cliente
        UsuarioClienteDTO cliente = obtenerDatosCliente(usuarioId, guestDTO).block();

        // Paso 2: Obtener Carrito
        CarritoClienteDTO carrito = carritoClient.findCarrito(usuarioId, sessionId).block();
        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException("No se puede procesar un pedido con un carrito vacío.");
        }

        // Paso 3: Validar Productos y Stock (La lógica de negocio principal)
        Set<PedidoItem> itemsDelPedido = new HashSet<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        // Iteramos sobre los items del carrito
        for (ItemCarritoClienteDTO itemCarrito : carrito.getItems()) {
            // 3a. Llamar a Inventario por CADA producto
            ProductoClienteDTO producto = inventarioClient.findProductoById(itemCarrito.getProductoId()).block();

            // 3b. Validación de Stock (Nuestra regla de negocio "simple y eficiente")
            if (producto == null || !producto.isDisponibleParaVenta()) {
                throw new StockInsuficienteException("El producto " + itemCarrito.getProductoId() + " no está disponible.");
            }
            if (producto.getStockActual() < itemCarrito.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre() +
                        ". Solicitados: " + itemCarrito.getCantidad() +
                        ", Disponibles: " + producto.getStockActual());
            }

            // 3c. Crear el PedidoItem (inmutable)
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProductoId(producto.getId());
            pedidoItem.setCantidad(itemCarrito.getCantidad());
            pedidoItem.setPrecioUnitario(producto.getPrecio()); // <-- Guardamos el precio "snapshot"

            BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(itemCarrito.getCantidad()));
            pedidoItem.setSubtotalLinea(subtotal);

            itemsDelPedido.add(pedidoItem);
            totalPedido = totalPedido.add(subtotal); // Sumamos al total
        }

        // Paso 4: Crear el Pedido en estado PENDIENTE
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId); // Nulo si es invitado
        pedido.setClienteNombre(cliente.getNombre() + " " + cliente.getApellido());
        pedido.setClienteEmail(cliente.getCorreo());
        pedido.setTotalPedido(totalPedido);
        pedido.setEstado(PedidoEstado.PENDIENTE);

        // Asociamos los items al pedido
        for (PedidoItem item : itemsDelPedido) {
            item.setPedido(pedido);
        }
        pedido.setItems(itemsDelPedido);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Paso 5: Crear Intención de Pago en Stripe
        String clientSecret = stripeService.crearPaymentIntent(totalPedido, pedidoGuardado.getId());

        // Actualizamos nuestro pedido con el ID de Stripe
        pedidoGuardado.setStripePaymentIntentId(clientSecret.split("_secret_")[0]); // (Truco para el mock)
        pedidoRepository.save(pedidoGuardado);

        // Paso 6: Devolver el ClientSecret al Frontend
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