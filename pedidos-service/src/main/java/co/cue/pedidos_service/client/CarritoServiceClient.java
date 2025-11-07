package co.cue.pedidos_service.client;

import co.cue.pedidos_service.models.dtos.client.CarritoClienteDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class CarritoServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String CARRITO_SERVICE_URL = "http://carrito-service";

    public Mono<CarritoClienteDTO> findCarrito(Long usuarioId, String sessionId) {
        String url = CARRITO_SERVICE_URL + "/api/carrito";

        return webClientBuilder.build()
                .get()
                .uri(url)
                // (Mentor): Pasamos las cabeceras que 'carrito-service' espera
                .header("X-Usuario-Id", (usuarioId != null) ? String.valueOf(usuarioId) : null)
                .header("X-Session-Id", sessionId)
                .retrieve()
                .bodyToMono(CarritoClienteDTO.class);
    }

    public Mono<Void> limpiarCarrito(Long usuarioId, String sessionId) {
        log.warn("[STUB] Limpiando carrito en carrito-service...");
        return Mono.empty();
    }
}
