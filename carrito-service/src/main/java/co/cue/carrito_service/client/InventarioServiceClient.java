package co.cue.carrito_service.client;

import co.cue.carrito_service.config.ProductoNotFoundException;
import co.cue.carrito_service.models.dtos.ProductoInventarioDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor

// Cliente para comunicarse con el servicio de inventario
public class InventarioServiceClient {

    // Constructor inyecta el WebClient.Builder
    private final WebClient.Builder webClientBuilder;

    /**
     * Llama al endpoint de inventario-service para validar un producto.
     */

    // Método que busca un producto por ID en inventario-service
    public ProductoInventarioDTO findProductoById(Long productoId) {
        // URL del endpoint de inventario-service
        String url = "http://inventario-service/api/inventario/productos/" + productoId;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                // Manejamos el 404 de inventario-service
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ProductoNotFoundException("Producto no encontrado en inventario: " + productoId)))
                // Convierte la respuesta al DTO
                .bodyToMono(ProductoInventarioDTO.class)
                .block();

    }
}
