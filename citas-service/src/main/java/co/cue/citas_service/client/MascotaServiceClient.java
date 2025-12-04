package co.cue.citas_service.client;

import co.cue.citas_service.dtos.MascotaClienteDTO;
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
public class MascotaServiceClient {

    private final WebClient.Builder webClientBuilder;

    // Nombre del servicio registrado en Eureka
    private static final String MASCOTAS_SERVICE_URL = "http://mascotas-service";

    public Mono<MascotaClienteDTO> findMascotaById(Long mascotaId) {
        String url = MASCOTAS_SERVICE_URL + "/api/mascotas/" + mascotaId;

        log.info("Consultando mascota ID: {} en mascotas-service", mascotaId);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(MascotaClienteDTO.class)
                .doOnError(e -> log.error("Error consultando mascota: {}", e.getMessage()));
    }

    /**
     * Obtiene el token JWT del contexto de seguridad para propagarlo.
     */
    private String getAuthenticatedToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        // Si estamos en un contexto sin usuario (ej. proceso batch), esto fallaría.
        // Para este caso de uso (crear cita), siempre hay un usuario.
        return "";
    }
    
    /**
     * Obtiene información básica de una mascota por ID sin requerir autenticación.
     * Útil para endpoints públicos como confirmación de citas.
     */
    public Mono<MascotaClienteDTO> findMascotaByIdPublico(Long mascotaId) {
        String url = MASCOTAS_SERVICE_URL + "/api/mascotas/public/" + mascotaId;
        
        log.info("Consultando mascota ID: {} en mascotas-service (público)", mascotaId);
        
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(MascotaClienteDTO.class)
                .doOnError(e -> log.error("Error consultando mascota pública: {}", e.getMessage()));
    }
}
