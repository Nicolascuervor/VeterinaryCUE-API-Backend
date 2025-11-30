package co.cue.inventario_service.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


/**
 * Configuración de seguridad para el microservicio de Inventario.
 * Define las reglas de control de acceso (Autorización) y el mecanismo de
 * validación de credenciales (Autenticación vía JWT).
 * Política de Seguridad del Inventario:
 * 1. Lectura Pública: El catálogo de productos debe ser visible para todos (invitados).
 * 2. Escritura Restringida: Solo administradores pueden crear, editar o eliminar productos.
 * 3. Operaciones Internas: Endpoints críticos para la operativa del sistema (descontar stock).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ADMIN_ROLE = "ADMIN";
    // Patrón base para las rutas de la API de inventario
    private static final String INVENTARIO_API_PATH = "/api/inventario/**";

    /**
     * Clave secreta compartida para la validación de la firma de los tokens JWT.
     * Debe coincidir con la utilizada en el authentication-service para firmar.
     */
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * Configura el decodificador de JWT.
     * Crea una instancia capaz de verificar la integridad y autenticidad del token
     * utilizando el algoritmo HMAC-SHA256 y la clave secreta configurada.
     * Esto permite al microservicio validar tokens localmente sin llamar al auth-service.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /**
     * Define la cadena de filtros de seguridad (SecurityFilterChain).
     * Establece las reglas de autorización HTTP específicas para este dominio.
     * - Deshabilita CSRF (no necesario para API REST stateless).
     * - Configura la gestión de sesiones como STATELESS (sin cookies).
     * - Habilita el servidor de recursos OAuth2 para procesar JWTs.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Aplicamos nuestro conversor de roles
                        )
                );

        http
                .authorizeHttpRequests(authz -> authz
                        // Reglas de Operaciones Internas
                        // Permite el acceso al endpoint de descuento de stock sin autenticación de usuario final.
                        // Nota de Arquitectura: Este endpoint es llamado por 'pedidos-service' (S2S).
                        // En un entorno productivo, esto debería protegerse por red (VPC interna)
                        // o mediante autenticación mTLS/Service Token, no dejarse 100% público.
                        .requestMatchers(HttpMethod.POST, "/api/inventario/productos/stock/descontar").permitAll()

                        // --- Reglas de Lectura (Catálogo Público) ---
                        // Cualquier usuario (autenticado o anónimo) puede ver productos y categorías.
                        // Fundamental para que la tienda sea navegable sin login previo.
                        .requestMatchers(HttpMethod.GET, INVENTARIO_API_PATH).permitAll()

                        // --- Reglas de Escritura (Gestión Administrativa) ---
                        // Solo los usuarios con rol ADMIN pueden modificar el inventario.
                        .requestMatchers(HttpMethod.POST, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)

                        // Cierre por defecto: Cualquier otra petición requiere autenticación válida.
                        .anyRequest().authenticated());
        return http.build();
    }

    /**
     * Personaliza la conversión de los claims del JWT a autoridades de Spring Security.
     * Extrae la lista de roles del claim "roles" y los convierte en objetos GrantedAuthority.
     * Configura el prefijo de autoridad como cadena vacía ("") para que coincida con
     * los nombres de roles definidos en nuestro sistema (ej. "ADMIN" en lugar de "ROLE_ADMIN"),
     * facilitando el uso de las expresiones hasRole().
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }

}