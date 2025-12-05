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
import co.cue.pedidos_service.pasarela.IPasarelaPagoGateway;
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
    // Repositorio para interactuar con la base de datos de pedidos.
    private final PedidoRepository pedidoRepository;
    // Cliente para obtener información del usuario desde el microservicio de Autenticación.
    private final AuthServiceClient authClient;
    // Cliente para obtener el carrito del usuario desde el microservicio Carrito.
    private final CarritoServiceClient carritoClient;
    // Cliente para obtener productos y validar stock en el microservicio Inventario.
    private final InventarioServiceClient inventarioClient;
    // Adaptador para interactuar con la pasarela de pagos (Stripe).
    private final IPasarelaPagoGateway pasarelaPagoGateway;


    @Override
    @Transactional  // Garantiza atomicidad en la creación del pedido.

    public CheckoutResponseDTO iniciarCheckout(Long usuarioId, String sessionId, CheckoutGuestRequestDTO guestDTO) {
        // Obtener datos del cliente (usuario registrado o invitado).
        UsuarioClienteDTO cliente = obtenerDatosCliente(usuarioId, guestDTO).block();
// Validar que los datos del cliente existan.
        if (cliente == null) {
            log.error("No se pudieron obtener los datos del cliente para el usuarioId: {}", usuarioId);
            throw new EntityNotFoundException("No se pudieron verificar los datos del cliente.");
        }
        // Recuperar el carrito desde el microservicio Carrito.
        CarritoClienteDTO carrito = carritoClient.findCarrito(usuarioId, sessionId).block();
        // Validar que el carrito no esté vacío.
        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoVacioException("No se puede procesar un pedido con un carrito vacío.");
        }
        // Conjunto donde se almacenararán los ítems del pedido.
        Set<PedidoItem> itemsDelPedido = new HashSet<>();
        // Total acumulado del pedido.
        BigDecimal totalPedido = BigDecimal.ZERO;
// Recorrer cada item del carrito.
        for (ItemCarritoClienteDTO itemCarrito : carrito.getItems()) {
            // Buscar información del producto en el servicio de inventario.
            ProductoClienteDTO producto = inventarioClient.findProductoById(itemCarrito.getProductoId()).block();
// Validar disponibilidad del producto.
            if (producto == null || !producto.isDisponibleParaVenta()) {
                throw new StockInsuficienteException("El producto " + itemCarrito.getProductoId() + " no está disponible.");
            }
            // Validar stock suficiente.
            if (producto.getStockActual() < itemCarrito.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre() +
                        ". Solicitados: " + itemCarrito.getCantidad() +
                        ", Disponibles: " + producto.getStockActual());
            }
// Construcción del item del pedido.
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProductoId(producto.getId());
            pedidoItem.setCantidad(itemCarrito.getCantidad());
            pedidoItem.setPrecioUnitario(producto.getPrecio());

            BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(itemCarrito.getCantidad()));
            pedidoItem.setSubtotalLinea(subtotal);

            itemsDelPedido.add(pedidoItem);
            totalPedido = totalPedido.add(subtotal);
        }
// Crear el objeto Pedido a partir de la información procesada.
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setClienteNombre(cliente.getNombre() + " " + cliente.getApellido());
        pedido.setClienteEmail(cliente.getCorreo());
        pedido.setTotalPedido(totalPedido);
        pedido.setEstado(PedidoEstado.PENDIENTE);

        for (PedidoItem item : itemsDelPedido) {
            item.setPedido(pedido);
        }
        pedido.setItems(itemsDelPedido);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        String clientSecret = pasarelaPagoGateway.crearIntencionDePago(
                totalPedido,
                pedidoGuardado.getId(),
                "cop"  // Pesos colombianos
        );

        // Extraer el PaymentIntent ID del clientSecret
        // Formato Stripe: pi_xxx_secret_yyy
        // Formato Simulado: pi_simulated_xxx_secret_simulated_yyy
        String paymentIntentId = clientSecret.split("_secret_")[0];
        pedidoGuardado.setStripePaymentIntentId(paymentIntentId);
        pedidoRepository.save(pedidoGuardado);

        return new CheckoutResponseDTO(pedidoGuardado.getId(), clientSecret);
    }

    /**
     * Obtiene los datos del cliente dependiendo si es usuario registrado
     * o usuario invitado.
     */
    private Mono<UsuarioClienteDTO> obtenerDatosCliente(Long usuarioId, CheckoutGuestRequestDTO guestDTO) {
        if (usuarioId != null) {
            return authClient.findUsuarioById(usuarioId);
        } else if (guestDTO != null && guestDTO.getClienteEmail() != null) {
            UsuarioClienteDTO dto = new UsuarioClienteDTO();
            dto.setNombre(guestDTO.getClienteNombre());
            dto.setApellido(""); // Invitado no tiene apellido separado
            dto.setCorreo(guestDTO.getClienteEmail());
            return Mono.just(dto);
        } else {
            return Mono.error(new IllegalArgumentException("Datos del cliente (usuarioId o guestDTO) son requeridos."));
        }
    }
}