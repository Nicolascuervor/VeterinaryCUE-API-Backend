package co.cue.mascotas_service.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
// Configura la seguridad del servicio, habilitando control por roles y validación JWT.
public class SecurityConfig {


    // Rol requerido para operaciones administrativas.
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String DUENIO_ROLE = "DUENIO";
    private static final String VETERINARIO_ROLE = "VETERINARIO";
    // Ruta protegida del módulo de mascotas.
    private static final String MASCOTAS_API_PATH = "/api/mascotas/**";

    @Value("${jwt.secret.key}")
    // Llave secreta usada para validar los tokens JWT.
    private String secretKey;

    @Bean
    // Decodifica y valida los tokens JWT usando la clave secreta configurada.
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    // Define las reglas de seguridad HTTP para el servicio.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactiva CSRF ya que la API es stateless.
                .csrf(AbstractHttpConfigurer::disable)
                
                // CORS se maneja en el API Gateway, no aquí para evitar duplicación de headers
                .cors(AbstractHttpConfigurer::disable)
                
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Habilita el servidor de recursos basado en JWT.
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        http
                // Define permisos por tipo de petición y rol.
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, MASCOTAS_API_PATH).authenticated()
                        .requestMatchers(HttpMethod.POST, MASCOTAS_API_PATH).hasAnyRole(ADMIN_ROLE,  DUENIO_ROLE,  VETERINARIO_ROLE)
                        .requestMatchers(HttpMethod.PUT, MASCOTAS_API_PATH).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, MASCOTAS_API_PATH).hasRole(ADMIN_ROLE)
                        // Cualquier otra ruta requiere autenticación.
                        .anyRequest().authenticated());
        return http.build();
    }


    @Bean
    // Convierte los roles contenidos en el JWT en autoridades para Spring Security.
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Indica el campo donde vienen los roles.
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        // No agrega prefijos como "ROLE_".
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }

}
