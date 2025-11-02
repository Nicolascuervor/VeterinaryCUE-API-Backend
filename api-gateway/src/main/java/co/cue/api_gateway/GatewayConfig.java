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

                // Ruta Pública
                .route("authentication_service_route", r -> r.path("/api/auth/**")
                        .uri("lb://authentication-service"))

                // Rutas Protegidas
                .route("citas_service_route", r -> r.path("/api/citas/**")
                        // (Colega Senior): Ahora usamos la variable del parámetro
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://citas-service"))

                .route("administration_service_route", r -> r.path("/api/admin/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://administration-service"))

                .route("mascotas_service_route", r -> r.path("/api/mascotas/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://mascotas-service"))

                .route("inventario_service_public", r -> r
                        .path("/api/inventario/**")
                        .and()
                        .method(HttpMethod.GET) // <-- Solo aplica a peticiones GET
                        .uri("lb://inventario-service"))

                // 3. Ruta Privada (Inventario - ESCRITURA)
                .route("inventario_service_private", r -> r
                        .path("/api/inventario/**")
                        .and()
                        .method(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://inventario-service"))

                .build();
    }
}
