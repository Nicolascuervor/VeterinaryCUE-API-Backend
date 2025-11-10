package co.cue.api_gateway;

import co.cue.api_gateway.config.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
                                           AuthenticationFilter authenticationFilter) {
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        var authFilter = authenticationFilter.apply(config);

        return builder.routes()

                // Ruta Pública (Autenticación)
                .route("authentication_service_route", r -> r.path("/api/auth/**")
                        .uri("lb://authentication-service"))

                // Pasamos todas las peticiones /api/carrito/** a través de nuestro filtro y luego al 'carrito-service'.
                .route("carrito_service_route", r -> r.path("/api/carrito/**")
                        .filters(f -> f.filter(authFilter)) // <-- Aplicamos el filtro
                        .uri("lb://carrito-service")) // <-- Apunta a Eureka

                .route("pedidos_service_route", r -> r.path("/api/pedidos/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://pedidos-service"))

                // RUTAS PROTEGIDAS
                .route("citas_service_route", r -> r.path("/api/citas/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://citas-service"))

                .route("administration_service_route", r -> r.path("/api/admin/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://administration-service"))

                .route("mascotas_service_route", r -> r.path("/api/mascotas/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://mascotas-service"))

                .route("inventario_service_public", r -> r
                        .path("/api/inventario/**")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://inventario-service"))

                .route("inventario_service_private", r -> r
                        .path("/api/inventario/**")
                        .and()
                        .method(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f.filter(authFilter)) // <-- Protegido
                        .uri("lb://inventario-service"))

                .route("agendamiento_service_route", r -> r.path("/api/agendamiento/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://agendamiento-service"))

                .route("historias_clinicas_service_route", r -> r.path("/api/historial-clinico/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://historias-clinicas-service"))

                .build();
    }
}