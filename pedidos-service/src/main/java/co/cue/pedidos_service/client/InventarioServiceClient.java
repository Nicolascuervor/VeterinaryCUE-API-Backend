package co.cue.pedidos_service.client;

import co.cue.pedidos_service.models.dtos.client.ProductoClienteDTO;
import co.cue.pedidos_service.models.dtos.requestdtos.StockReductionDTO;
import co.cue.pedidos_service.models.entities.PedidoItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Set;

@Component
// Genera automáticamente un constructor con todos los atributos usando Lombok
@AllArgsConstructor
// Habilita un logger (log) para registrar información y errores
@Slf4j
public class InventarioServiceClient {
    // Builder de WebClient para realizar peticiones HTTP reactivas a otros microservicios
    private final WebClient.Builder webClientBuilder;
    // URL base del microservicio de inventario
    private static final String INVENTARIO_SERVICE_URL = "http://inventario-service";

    // Método que descuenta el stock de los productos incluidos en un conjunto de items del pedido
    public Mono<Void> descontarStock(Set<PedidoItem> items) {
        // Log informativo indicando que se está enviando la solicitud al servicio de inventario
        log.info("Enviando solicitud de descuento de stock a inventario-service...");
        // Convierte los items del pedido en una lista de DTOs para el payload de la petición
        List<StockReductionDTO> payload = items.stream()
                // Mapea cada item a un objeto StockReductionDTO con productoId y cantidad
                .map(item -> new StockReductionDTO(item.getProductoId(), item.getCantidad()))
                // Convierte el stream en lista
                .toList();

        // Construye y ejecuta la solicitud POST para descontar stock
        return webClientBuilder.build()  // Construye el WebClient
                .post()    // Define el método HTTP POST
                .uri(INVENTARIO_SERVICE_URL + "/api/inventario/productos/stock/descontar")   // URL del endpoint
                .bodyValue(payload)   // Envía el cuerpo con la lista de reducciones de stock
                .retrieve()    // Ejecuta la solicitud
                .bodyToMono(Void.class)  // La respuesta no tiene contenido, por eso Void
                .doOnSuccess(v -> log.info("Stock descontado exitosamente."))   // Log de éxito si la operación se completó sin errores
                .doOnError(e -> log.error("Error al descontar stock: {}", e.getMessage()));    // Log de error si algo salió mal
    }

    // Método que obtiene los datos de un producto por su ID desde el microservicio de inventario
    public Mono<ProductoClienteDTO> findProductoById(Long productoId) {
        // Construye la URL del endpoint que retorna el producto por ID
        String url = INVENTARIO_SERVICE_URL + "/api/inventario/productos/" + productoId;


        // Realiza la solicitud GET al microservicio de inventario
        return webClientBuilder.build()    // Construye el WebClient
                .get()     // Método HTTP GET
                .uri(url)    // URL de la petición
                .retrieve()    // Ejecuta la solicitud
                .bodyToMono(ProductoClienteDTO.class);    // Convierte el cuerpo de la respuesta al DTO ProductoClienteDTO
    }

}
