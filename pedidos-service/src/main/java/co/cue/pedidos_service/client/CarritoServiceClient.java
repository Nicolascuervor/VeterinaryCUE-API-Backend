package co.cue.pedidos_service.client;

import co.cue.pedidos_service.models.dtos.client.CarritoClienteDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
// Genera automáticamente un constructor con todos los atributos usando Lombok
@AllArgsConstructor
// Habilita un logger (log) usando Lombok para registrar mensajes
@Slf4j
public class CarritoServiceClient {
    // Builder de WebClient para realizar peticiones HTTP reactivas
    private final WebClient.Builder webClientBuilder;
    // URL base del microservicio de carrito
    private static final String CARRITO_SERVICE_URL = "http://carrito-service";
    // Método que consulta el carrito asociado a un usuario o una sesión
    public Mono<CarritoClienteDTO> findCarrito(Long usuarioId, String sessionId) {
        // Construye la URL completa para consultar el carrito
        String url = CARRITO_SERVICE_URL + "/api/carrito";

        return webClientBuilder.build()                           // Construye el WebClient
                .get()                                            // Define el método HTTP GET
                .uri(url)                                         // Asigna la URL de la petición
                .header("X-Usuario-Id", (usuarioId != null) ? String.valueOf(usuarioId) : null)  // Envía header con ID del usuario si existe
                .header("X-Session-Id", sessionId)   // Envía el ID de sesión para identificar carritos anónimos
                .retrieve()                                      // Ejecuta la solicitud
                .bodyToMono(CarritoClienteDTO.class);           // Convierte la respuesta a CarritoClienteDTO
    }
    // Método que limpia (vacía) el carrito de un usuario o sesión
    public Mono<Void> limpiarCarrito(Long usuarioId, String sessionId) {
        //Construye la URL completa para la acción de limpieza
        String url = CARRITO_SERVICE_URL + "/api/carrito";

        return webClientBuilder.build()           //Contruye el WebCliente
                .delete()                         //Define el método HTTP DELETE
                .uri(url)                         //Asigna la URL de la petición
                .header("X-Usuario-Id", (usuarioId != null) ? String.valueOf(usuarioId) : null)    //Header con ID del usuario
                .header("X-Session-Id", sessionId)    //Header con el ID de sesión
                .retrieve()                       //Ejecuta la solicitud
                .bodyToMono(Void.class);          //La respuesta no contiene cuerpo, por eso Void.class
    }
}
