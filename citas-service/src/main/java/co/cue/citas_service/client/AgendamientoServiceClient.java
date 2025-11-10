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
    private final String AGENDAMIENTO_SERVICE_URL = "http://agendamiento-service";

    // 1. Obtener detalles de un Servicio
    public Mono<ServicioClienteDTO> getServicioById(Long servicioId) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/servicios-admin/" + servicioId;

        return webClientBuilder.build()
                .get().uri(url)
                // (Mentor): ¡SOLUCIÓN! Propagamos el token del usuario actual.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(ServicioClienteDTO.class);
    }

    // 2. Obtener detalles de los Slots
    public Mono<List<DisponibilidadClienteDTO>> getDisponibilidadByIds(List<Long> slotIds) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/disponibilidad/slots/list";

        return webClientBuilder.build()
                .post().uri(url)
                .bodyValue(slotIds)
                // (Mentor): ¡SOLUCIÓN! Propagamos el token del usuario actual.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .bodyToFlux(DisponibilidadClienteDTO.class)
                .collectList();
    }

    // 3. Reservar los Slots
    public Mono<Void> reservarSlots(ReservaRequestDTO reservaDTO) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/disponibilidad/reservar";

        return webClientBuilder.build()
                .post().uri(url)
                .bodyValue(reservaDTO)
                // (Mentor): ¡SOLUCIÓN! Propagamos el token del usuario actual.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(Void.class);
    }

    // 4. Liberar Slots
    public Mono<Void> liberarSlots(Long citaId) {
        String url = AGENDAMIENTO_SERVICE_URL + "/api/agendamiento/disponibilidad/liberar/" + citaId;

        return webClientBuilder.build()
                .post().uri(url)
                // (Mentor): ¡SOLUCIÓN! Propagamos el token del usuario actual.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * (Profesor): Helper privado para obtener el token del usuario
     * que *originó* la llamada a 'citas-service'.
     */
    private String getAuthenticatedToken() {
        // Obtenemos el contexto de seguridad de Spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificamos que sea un token JWT
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            // Devolvemos el string puro del token
            return jwtAuth.getToken().getTokenValue();
        }

        // (Arquitecto): Si no hay token, lanzamos un error que detendrá
        // la operación antes de hacer una llamada anónima.
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}