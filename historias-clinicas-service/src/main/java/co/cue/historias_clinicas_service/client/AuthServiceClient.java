package co.cue.historias_clinicas_service.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class AuthServiceClient {

    private final WebClient.Builder webClientBuilder;

    private static final String AUTH_SERVICE_URL = "http://authentication-service";

    /**
     * Obtiene información del usuario por su ID desde el servicio de autenticación.
     */
    public Mono<UsuarioClienteDTO> obtenerUsuarioPorId(Long id) {
        String url = AUTH_SERVICE_URL + "/api/auth/" + id;

        log.info("Consultando datos del usuario ID: {} en authentication-service", id);

        try {
            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                    .retrieve()
                    .bodyToMono(UsuarioClienteDTO.class)
                    .doOnError(e -> log.error("Error obteniendo usuario: {}", e.getMessage()));
        } catch (IllegalStateException e) {
            // Si no hay token (por ejemplo, cuando se crea desde un evento Kafka),
            // intentamos sin token (puede que el endpoint sea público o necesite otra estrategia)
            log.warn("No se pudo obtener token JWT, intentando sin autenticación para usuario ID: {}", id);
            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(UsuarioClienteDTO.class)
                    .doOnError(err -> log.error("Error obteniendo usuario sin token: {}", err.getMessage()));
        }
    }

    /**
     * Obtiene el token JWT del contexto de seguridad del usuario actual.
     */
    private String getAuthenticatedToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}

