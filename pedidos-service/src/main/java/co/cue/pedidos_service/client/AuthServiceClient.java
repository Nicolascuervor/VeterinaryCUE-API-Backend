package co.cue.pedidos_service.client;

import co.cue.pedidos_service.models.dtos.client.UsuarioClienteDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String AUTH_SERVICE_URL = "http://authentication-service";

    public Mono<UsuarioClienteDTO> findUsuarioById(Long usuarioId) {
        return webClientBuilder.build()
                .get()
                .uri(AUTH_SERVICE_URL + "/api/auth/" + usuarioId)
                // (Mentor): Necesitaremos propagar el Token JWT del usuario
                // actual para que auth-service nos autorice. Lo haremos después.
                .retrieve()
                .bodyToMono(UsuarioClienteDTO.class);
    }
}
