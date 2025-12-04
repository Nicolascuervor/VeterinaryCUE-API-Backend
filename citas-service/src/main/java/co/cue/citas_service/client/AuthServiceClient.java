package co.cue.citas_service.client;
import co.cue.citas_service.dtos.UsuarioClienteDTO;
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

    // Nombre del servicio registrado en Eureka
    private static final String AUTH_SERVICE_URL = "http://authentication-service";

    public Mono<UsuarioClienteDTO> obtenerUsuarioPorId(Long id) {
        String url = AUTH_SERVICE_URL + "/api/auth/" + id;

        log.info("Consultando datos del usuario ID: {} en authentication-service", id);

        return webClientBuilder.build()
                .get()
                .uri(url)
                // Pasamos el token actual para mantener la cadena de seguridad
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(UsuarioClienteDTO.class)
                .doOnError(e -> log.error("Error obteniendo usuario: {}", e.getMessage()));
    }

    private String getAuthenticatedToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
    
    /**
     * Obtiene información básica de un usuario por ID sin requerir autenticación.
     * Útil para endpoints públicos como confirmación de citas.
     */
    public Mono<UsuarioClienteDTO> obtenerUsuarioPorIdPublico(Long id) {
        String url = AUTH_SERVICE_URL + "/api/auth/public/" + id;
        
        log.info("Consultando datos del usuario ID: {} en authentication-service (público)", id);
        
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(UsuarioClienteDTO.class)
                .doOnError(e -> log.error("Error obteniendo usuario público: {}", e.getMessage()));
    }
}