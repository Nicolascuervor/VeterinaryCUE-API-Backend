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
//Indica que esta clase es un componente de Spring para ser detectado automáticamente
@Component
//Genera un constructor con todos los atributos utilizando Lombok
@AllArgsConstructor
public class AuthServiceClient {
    //Inyección del WebClient Builder para realizar peticiones HTTP reactivas
    private final WebClient.Builder webClientBuilder;
    //URL base del servicio de autenticación
    private static final String AUTH_SERVICE_URL = "http://authentication-service";
    //Método que consulta un usuario por su ID en el servicio de autenticación
    public Mono<UsuarioClienteDTO> findUsuarioById(Long usuarioId) {
        return webClientBuilder.build()      //Construye el WebClient
                .get()   //Define el método HTTP GET
                .uri(AUTH_SERVICE_URL + "/api/auth/" + usuarioId)  //Construye la URI completa
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticatedToken())  //Añade el header con el token JWT
                .retrieve()  //Ejecuta la solicitud y obtiene la respuesta
                .bodyToMono(UsuarioClienteDTO.class);  //Convierte el cuerpo de respuesta al tipo UsuarioClienteDTO
    }

    //Método privado que obtiene el token JWT del contexto de seguridad actual
    private String getAuthenticatedToken() {
        //Obtiene la autenticación actual del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Verifica si la autenticación es del tipo JWTAuthenticationToken
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        //Lanza excepción si no encuentra un token adecuado
        throw new IllegalStateException("No se pudo obtener el token JWT del contexto de seguridad");
    }
}
