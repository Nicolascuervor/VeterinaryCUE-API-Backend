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

    // WebClient inyectado para realizar llamadas HTTP a otros microservicios.
    private final WebClient.Builder webClientBuilder;

    // URL base del microservicio mascotas.
    private static final String MASCOTAS_SERVICE_URL = "http://mascotas-service";

    /**
     * Llama al microservicio de mascotas para obtener información de una mascota por ID.
     * Incluye el token JWT del usuario autenticado en el encabezado.
     */

    public Mono<MascotaClienteDTO> findMascotaById(Long mascotaId) {

        String url = MASCOTAS_SERVICE_URL + "/api/mascotas/" + mascotaId;

        return webClientBuilder.build()
                .get()
                .uri(url)
                // Se envía el token JWT actual al servicio de mascotas.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                // Manejo de error 404 desde el microservicio mascotas.
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new MascotaNotFoundException("Mascota no encontrada en mascotas-service: " + mascotaId)))
                // Convierte la respuesta JSON al DTO correspondiente.
                .bodyToMono(MascotaClienteDTO.class);
    }

    /**
     * Obtiene el token JWT del contexto de seguridad del usuario actual.
     * Necesario para reenviar el token en llamadas entre microservicios.
     */
    private String getAuthenticatedToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        // Si no hay token disponible, significa que la petición no venía autenticada.
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}