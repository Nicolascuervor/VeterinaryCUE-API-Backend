package co.cue.citas_service.client;


import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.citas_service.dtos.DisponibilidadClienteDTO;
import co.cue.citas_service.dtos.ServicioClienteDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.List;
@Component
@AllArgsConstructor
public class AgendamientoServiceClient {

    private final WebClient.Builder webClientBuilder;

    // URL base del microservicio de agendamiento
    private static final String AGENDAMIENTO_SERVICE_URL = "http://agendamiento-service";

    // Prefijo para enviar el token JWT
    private static final String BEARER_PREFIX = "Bearer ";

    // Obtiene un servicio específico por su ID
    public Mono<ServicioClienteDTO> getServicioById(Long servicioId) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/servicios-admin/" + servicioId;

        return webClientBuilder.build()
                .get().uri(url)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(ServicioClienteDTO.class);
    }

    // Obtiene la disponibilidad de una lista de slots por sus IDs
    public Mono<List<DisponibilidadClienteDTO>> getDisponibilidadByIds(List<Long> slotIds) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/disponibilidad/slots/list";

        return webClientBuilder.build()
                .post().uri(url)
                .bodyValue(slotIds)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAuthenticatedToken())
                .retrieve()
                .bodyToFlux(DisponibilidadClienteDTO.class)
                .collectList();
    }

    // Envía la solicitud para reservar los slots seleccionados
    public Mono<Void> reservarSlots(ReservaRequestDTO reservaDTO) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/disponibilidad/reservar";

        return webClientBuilder.build()
                .post().uri(url)
                .bodyValue(reservaDTO)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Solicita liberar slots cuando una cita es eliminada o modificada
    public Mono<Void> liberarSlots(Long citaId) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/disponibilidad/liberar/" + citaId;

        return webClientBuilder.build()
                .post().uri(url)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Obtiene el token JWT del usuario autenticado
    private String getAuthenticatedToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica si el usuario está autenticado con un JWT
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}