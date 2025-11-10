package co.cue.historias_clinicas_service.config;

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
@EnableMethodSecurity // (Mentor): Permite usar @PreAuthorize si lo necesitamos a futuro
public class SecurityConfig {

    // (Mentor): Definimos los roles que usa este servicio
    // Spring automáticamente añade el prefijo "ROLE_"
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String VETERINARIO_ROLE = "VETERINARIO";
    private static final String DUEÑO_ROLE = "DUEÑO";

    // (Mentor): Definimos la ruta base de nuestro controlador
    private static final String HISTORIAL_API_PATH = "/api/historial-clinico/**";

    @Value("${jwt.secret.key}") //
    private String secretKey;

    /**
     * (Colega Senior): Bean idéntico al esqueleto.
     * Lee la clave secreta y crea el decodificador de JWT.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /**
     * (Arquitecto): Esta es la lógica de autorización adaptada
     * a nuestras reglas de negocio.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // (Mentor): Deshabilitamos CSRF (común en APIs stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // (Mentor): No creamos sesiones HTTP
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // (Mentor): Configuramos la validación del token JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        http
                .authorizeHttpRequests(authz -> authz
                        // (Regla 1): Ver historiales (GET) es para Dueños, Vets, y Admins.
                        // La lógica de *cuál* historial puede ver se delega al ServiceImpl.
                        .requestMatchers(HttpMethod.GET, HISTORIAL_API_PATH)
                        .hasAnyRole(DUEÑO_ROLE, VETERINARIO_ROLE, ADMIN_ROLE)

                        // (Regla 2): Crear historiales (POST) es solo para Veterinarios.
                        .requestMatchers(HttpMethod.POST, HISTORIAL_API_PATH)
                        .hasRole(VETERINARIO_ROLE)

                        // (Regla 3): Actualizar (PUT) es solo para Veterinarios.
                        .requestMatchers(HttpMethod.PUT, HISTORIAL_API_PATH)
                        .hasRole(VETERINARIO_ROLE)

                        // (Regla 4): Borrar (DELETE) es para Vets o Admins.
                        .requestMatchers(HttpMethod.DELETE, HISTORIAL_API_PATH)
                        .hasAnyRole(VETERINARIO_ROLE, ADMIN_ROLE)

                        // (Regla 5): Cualquier otra petición debe estar, al menos, autenticada.
                        .anyRequest().authenticated());

        return http.build();
    }


    /**
     * (Colega Senior): Bean idéntico al esqueleto.
     * Le dice a Spring Security cómo encontrar los roles ("roles")
     * dentro del token JWT.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // El nombre del Claim en el JWT
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // No añadimos prefijo "ROLE_"

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }
}