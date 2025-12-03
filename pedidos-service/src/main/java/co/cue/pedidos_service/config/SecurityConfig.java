package co.cue.pedidos_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
// Indica que esta clase contiene configuraciones para Spring
@Configuration
// Habilita la seguridad web en la aplicación
@EnableWebSecurity
// Permite usar anotaciones de seguridad en métodos (@PreAuthorize, @RolesAllowed, etc.)
@EnableMethodSecurity
public class SecurityConfig {
    // Obtiene la clave secreta para validar JWT desde application.properties
    @Value("${jwt.secret.key}")
    private String secretKey;

    // Bean encargado de decodificar y validar tokens JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        // Decodifica la clave secreta desde Base64
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        // Crea una clave secreta HMAC-SHA256 para validar las firmas del JWT
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        // Construye un decodificador JWT usando la clave secreta
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    // Configura la cadena de filtros de seguridad (Spring Security Filter Chain)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactiva CSRF porque se utiliza un esquema stateless basado en JWT
                .csrf(csrf -> csrf.disable())
                
                // CORS se maneja en el API Gateway, no aquí para evitar duplicación de headers
                .cors(cors -> cors.disable())
                
                // Define que la aplicación no mantiene sesiones en el servidor (STATeless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Configura la aplicación como un Resource Server OAuth2 con soporte para JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                // Indica cómo convertir el JWT a un Authentication object
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        http
                // Configura reglas de autorización para los endpoints
                .authorizeHttpRequests(authz -> authz
                        // Endpoint público (Stripe Webhook)
                        .requestMatchers("/api/pedidos/stripe/webhook").permitAll()
                        // Permite crear el proceso de checkout sin autenticación
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/checkout").permitAll()
                        // Requiere autenticación para cualquier endpoint dentro de /api/pedidos/**
                        .requestMatchers("/api/pedidos/**").authenticated()
                        // Cualquier otra solicitud también requiere autenticación
                        .anyRequest().authenticated());
        // Construye y retorna la cadena de seguridad configurada
        return http.build();
    }
    //Bean que convierte un JWT en un objeto Authentication con roles válidos
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Conversor que extrae autoridades desde el claim "roles"
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");  // claim donde vienen los roles
        grantedAuthoritiesConverter.setAuthorityPrefix("");             // sin prefijo "ROLE_"
        // Conversor final que incorporará las autoridades extraídas
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        // Retorna el conversor completo
        return jwtConverter;
    }
}
