package co.cue.citas_service.client;
import co.cue.citas_service.dtos.OcupacionRequestDTO;
import co.cue.citas_service.dtos.OcupacionResponseDTO;
import co.cue.citas_service.dtos.ServicioClienteDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


@Component
@AllArgsConstructor
@Slf4j
public class AgendamientoServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String AGENDAMIENTO_SERVICE_URL = "http://agendamiento-service";
    private static final String BEARER_PREFIX = "Bearer ";

    // --- MÉTODOS DE SERVICIOS (Se mantienen igual) ---

    public Mono<ServicioClienteDTO> getServicioById(Long servicioId) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/servicios-admin/" + servicioId;
        return webClientBuilder.build()
                .get().uri(url)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(ServicioClienteDTO.class);
    }

    // --- NUEVOS MÉTODOS DE RESERVA ---

    /**
     * Solicita al servicio de agendamiento que cree una ocupación (reserva) para una cita.
     * Ahora enviamos un rango de fechas, no una lista de IDs de slots.
     */
    public Mono<OcupacionResponseDTO> reservarEspacio(OcupacionRequestDTO reservaDTO) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/interno/reservar";

        log.info("Enviando solicitud de reserva a Agendamiento: VetId={}, Inicio={}",
                reservaDTO.getVeterinarioId(), reservaDTO.getFechaInicio());

        return webClientBuilder.build()
                .post().uri(url)
                .bodyValue(reservaDTO)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(OcupacionResponseDTO.class)
                // Manejo básico de errores: si agendamiento dice "409 Conflict" o "400 Bad Request"
                // WebClient lanzará una excepción que CitaService deberá capturar.
                .doOnError(e -> log.error("Error reservando espacio en agendamiento-service: {}", e.getMessage()));
    }

    /**
     * Libera el espacio eliminando la ocupación asociada a la cita.
     */
    public Mono<Void> liberarEspacio(Long citaId) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/interno/liberar/" + citaId;

        log.info("Solicitando liberar espacio para Cita ID: {}", citaId);

        return webClientBuilder.build()
                .delete().uri(url)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> log.error("Error liberando espacio para Cita ID {}: {}", citaId, e.getMessage()));
    }

    private String getAuthenticatedToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        // En un entorno real, podrías manejar el caso de llamadas internas sin usuario (Service Account),
        // pero por ahora asumimos que siempre hay un usuario logueado.
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}