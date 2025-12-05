package co.cue.api_gateway;

import co.cue.api_gateway.config.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * Configuración de las rutas del API Gateway.
 * Esta clase define el "mapa de tráfico" de nuestra arquitectura de microservicios.
 * En lugar de usar solo configuración en propiedades (YAML/Properties), utilizamos
 * Java Config para tener mayor flexibilidad y control sobre los filtros aplicados
 * a cada ruta específica.
 * Aquí se orquesta la integración con Eureka (usando el esquema {lb://}) y
 * la seguridad (aplicando nuestro AuthenticationFilter).
 */
@Configuration
public class GatewayConfig {

    /**
     * Define y construye las rutas del Gateway.
     * Utiliza el patrón Builder para encadenar definiciones de rutas. Cada ruta específica:
     ID: Identificador único de la ruta.
     Predicado (Path): Qué URL debe coincidir para activar esta ruta.
     Filtros: Qué lógica ejecutar antes/después (ej. Autenticación).
     URI: A dónde enviar la petición (usando balanceo de carga)
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
                                           AuthenticationFilter authenticationFilter) {

        // Preparamos una instancia de configuración para nuestro filtro de autenticación.
        // Aunque vacía por ahora, es requerida por la API del filtro tipo Factory.
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        var authFilter = authenticationFilter.apply(config);

        return builder.routes()

                // RUTAS PÚBLICAS

                // Servicio de Autenticación
                // Permite el registro y login sin necesidad de token previo.
                .route("authentication_service_route", r -> r.path("/api/auth/**")
                        .uri("lb://authentication-service"))

                // Endpoints públicos para confirmación de citas (sin autenticación)
                .route("citas_service_public", r -> r.path("/api/citas/public/**")
                        .uri("lb://citas-service"))
                
                // Endpoints públicos para obtener información básica (sin autenticación)
                .route("auth_service_public", r -> r.path("/api/auth/public/**")
                        .uri("lb://authentication-service"))
                
                .route("mascotas_service_public", r -> r.path("/api/mascotas/public/**")
                        .uri("lb://mascotas-service"))

                // Ruta específica para imágenes de productos (debe ir antes de las rutas generales de inventario)
                // Permite acceso público a las imágenes sin autenticación
                .route("inventario_uploads_public", r -> r.path("/api/inventario/uploads/**")
                        .uri("lb://inventario-service"))


                // RUTAS PROTEGIDAS

                // Servicio de Carrito:
                // Requiere saber quién es el usuario, por lo que aplicamos el filtro.
                .route("carrito_service_route", r -> r.path("/api/carrito/**")
                        .filters(f -> f.filter(authFilter)) // Aplicación del filtro de seguridad
                        .uri("lb://carrito-service")) // <-- 'lb' indica Load Balanced (busca en Eureka)

                // Servicio de Pedidos
                .route("pedidos_service_route", r -> r.path("/api/pedidos/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://pedidos-service"))

                // Servicio de Citas
                .route("citas_service_route", r -> r.path("/api/citas/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://citas-service"))

                // Servicio de Administración
                .route("administration_service_route", r -> r.path("/api/admin/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://administration-service"))

                // Servicio de Mascotas
                .route("mascotas_service_route", r -> r.path("/api/mascotas/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://mascotas-service"))

                // RUTAS HÍBRIDAS (Inventario)

                // Caso Especial: Lectura Pública (GET)
                // Cualquier usuario (incluso no logueado) debería poder ver el catálogo de productos.
                .route("inventario_service_public", r -> r
                        .path("/api/inventario/**")
                        .and()
                        .method(HttpMethod.GET) // Solo coincide si el verbo es GET
                        .uri("lb://inventario-service"))

                // Caso Especial: Escritura Privada (POST, PUT, DELETE)
                // Solo usuarios autenticados (y con roles, validado en el ms) pueden modificar el inventario.
                .route("inventario_service_private", r -> r
                        .path("/api/inventario/**")
                        .and()
                        .method(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f.filter(authFilter)) // Protegido
                        .uri("lb://inventario-service"))

                // Servicio de Agendamiento (Disponibilidad de veterinarios):
                .route("agendamiento_service_route", r -> r.path("/api/agendamiento/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://agendamiento-service"))

                // Servicio de Historias Clínicas:
                .route("historias_clinicas_service_route", r -> r.path("/api/historial-clinico/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://historias-clinicas-service"))

                .build();
    }
}