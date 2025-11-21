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
@AllArgsConstructor
@Slf4j
public class InventarioServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String INVENTARIO_SERVICE_URL = "http://inventario-service";

    public Mono<Void> descontarStock(Set<PedidoItem> items) {
        log.info("Enviando solicitud de descuento de stock a inventario-service...");
        List<StockReductionDTO> payload = items.stream()
                .map(item -> new StockReductionDTO(item.getProductoId(), item.getCantidad()))
                .toList();
        return webClientBuilder.build()
                .post()
                .uri(INVENTARIO_SERVICE_URL + "/api/inventario/productos/stock/descontar")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Stock descontado exitosamente."))
                .doOnError(e -> log.error("Error al descontar stock: {}", e.getMessage()));
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
