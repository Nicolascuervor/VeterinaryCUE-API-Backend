package co.cue.historias_clinicas_service.client;

import co.cue.historias_clinicas_service.client.exceptions.MascotaNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Component
@AllArgsConstructor
public class MascotaServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String MASCOTAS_SERVICE_URL = "http://mascotas-service";

    public Mono<MascotaClienteDTO> findMascotaById(Long mascotaId) {

        String url = MASCOTAS_SERVICE_URL + "/api/mascotas/" + mascotaId;

        return webClientBuilder.build()
                .get()
                .uri(url)
                // (Mentor): ¡SOLUCIÓN! Propagamos el token del usuario actual.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new MascotaNotFoundException("Mascota no encontrada en mascotas-service: " + mascotaId)))
                .bodyToMono(MascotaClienteDTO.class);
    }


    private String getAuthenticatedToken() {
        // Obtenemos el contexto de seguridad de Spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificamos que sea un token JWT
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            // Devolvemos el string puro del token
            return jwtAuth.getToken().getTokenValue();
        }

        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}