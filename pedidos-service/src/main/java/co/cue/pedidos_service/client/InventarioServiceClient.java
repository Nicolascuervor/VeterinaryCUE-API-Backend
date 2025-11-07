package co.cue.pedidos_service.client;

import ch.qos.logback.classic.Logger;
import co.cue.pedidos_service.models.dtos.client.ProductoClienteDTO;
import co.cue.pedidos_service.models.entities.PedidoItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class InventarioServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String INVENTARIO_SERVICE_URL = "http://inventario-service";

    public Mono<Void> descontarStock(Set<PedidoItem> items) {
        log.warn("[STUB] Descontando stock en inventario-service...");
        return Mono.empty();
    }

    public Mono<ProductoClienteDTO> findProductoById(Long productoId) {
        String url = INVENTARIO_SERVICE_URL + "/api/inventario/productos/" + productoId;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ProductoClienteDTO.class);
    }

}
