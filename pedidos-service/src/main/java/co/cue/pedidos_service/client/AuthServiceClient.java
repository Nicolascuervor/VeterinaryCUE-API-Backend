package co.cue.pedidos_service.client;

import co.cue.pedidos_service.models.dtos.client.UsuarioClienteDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Component
@AllArgsConstructor
public class AuthServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String AUTH_SERVICE_URL = "http://authentication-service";

    public Mono<UsuarioClienteDTO> findUsuarioById(Long usuarioId) {
        return webClientBuilder.build()
                .get()
                .uri(AUTH_SERVICE_URL + "/api/auth/" + usuarioId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(UsuarioClienteDTO.class);
    }


    private String getAuthenticatedToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}
