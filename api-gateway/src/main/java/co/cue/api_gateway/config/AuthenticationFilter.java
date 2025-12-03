package co.cue.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtro de autenticación personalizado para la API Gateway.
 * Este componente actúa como un interceptor de seguridad que se ejecuta antes de enrutar
 * las peticiones a los microservicios downstream. Su responsabilidad principal es validar
 * la presencia y autenticidad del token JWT en las cabeceras HTTP.
 * Si el token es válido, extrae el ID del usuario y lo inyecta como una cabecera personalizada
 * para que los servicios internos puedan identificar al usuario sin necesidad
 * de volver a procesar el token, centralizando así la carga de seguridad.
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    /**
     * Constructor para la inyección de dependencias.
     */
    @Autowired
    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    /**
     * Aplica la lógica de filtrado a las peticiones entrantes.
     * Este método contiene el flujo principal de seguridad: verificación de cabeceras,
     * validación del token y enriquecimiento de la petición con datos del usuario.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            // Permitir peticiones OPTIONS (preflight CORS) sin autenticación
            // Estas peticiones son enviadas automáticamente por el navegador antes de la petición real
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }

            // Validación de Cabeceras
            // Primero verificamos si la petición contiene la cabecera de autorización estándar.
            // Si no existe, pasamos la petición tal cual (otros filtros o el servicio final decidirán qué hacer),
            // aunque para rutas protegidas esto usualmente resultará en un error más adelante.
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return this.onError(exchange, "No se encontró token JWT válido", HttpStatus.UNAUTHORIZED);
            }

            // Extracción y Validación del Token
            // Se remueve el prefijo "Bearer" para obtener el token JWT puro.
            String token = authHeader.substring(7);

            // Delegamos la validación criptográfica (firma) y de expiración al servicio de utilidad.
            if (!jwtUtil.isTokenValid(token)) {
                return this.onError(exchange, "Token JWT inválido o expirado", HttpStatus.UNAUTHORIZED);
            }

            // Propagación de Identidad
            // Si el token es válido, extraemos la identidad del usuario (ID) de los claims.
            // Transformamos un token opaco en un dato explícito para los microservicios.
            Long usuarioId = jwtUtil.extractUsuarioId(token);

            // Mutamos el objeto de intercambio (exchange) para agregar la nueva cabecera.
            // Esto permite que los microservicios downstream (como Pedidos o Citas) lean directamente
            // quién es el usuario sin tener que decodificar el JWT ellos mismos.
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(r -> r.header("X-Usuario-Id", String.valueOf(usuarioId)))
                    .build();

            // Continuamos con la cadena de filtros usando la petición enriquecida.
            return chain.filter(mutatedExchange);
        };
    }

    /**
     * Maneja los errores de autenticación devolviendo una respuesta controlada al cliente.
     * Este método detiene la propagación de la petición hacia los microservicios y retorna
     * inmediatamente un código de estado HTTP (generalmente 401 Unauthorized) junto con un mensaje.
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");

        // Escribimos el mensaje de error en el cuerpo de la respuesta.
        return response.writeWith(Mono.just(response.bufferFactory().wrap(err.getBytes())));
    }

    /**
     * Clase de configuración para el filtro.
     * Aunque actualmente no tiene propiedades, es requerida por la arquitectura de Spring Cloud Gateway
     * para instanciar el filtro de fábrica. Podría usarse en el futuro para parámetros como
     * roles permitidos o rutas excluidas.
     */
    @SuppressWarnings("java:S2094") // Suprimimos advertencia de clase vacía intencional
    public static class Config { }
}
