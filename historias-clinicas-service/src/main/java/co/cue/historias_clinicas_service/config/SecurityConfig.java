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
@EnableMethodSecurity
public class SecurityConfig {


    private static final String ADMIN_ROLE = "ADMIN";
    private static final String VETERINARIO_ROLE = "VETERINARIO";
    private static final String DUENIO_ROLE = "DUENIO";

    private static final String HISTORIAL_API_PATH = "/api/historial-clinico/**";

    @Value("${jwt.secret.key}") //
    private String secretKey;

    /**
     * (Colega Senior): Bean idéntico al esqueleto.
     * Lee la clave secreta y crea el decodificador de JWT.
     */

    /**
     * Decodifica el JWT usando una clave secreta HMAC.
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

    /**
     * Configuración general de seguridad para el servicio.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        http
                .authorizeHttpRequests(authz -> authz

                        // GET → disponible para Dueño, Veterinario y Admin
                        .requestMatchers(HttpMethod.GET, HISTORIAL_API_PATH)
                        .hasAnyRole(DUENIO_ROLE, VETERINARIO_ROLE, ADMIN_ROLE)

                        // POST → solo Veterinario (crea registros)
                        .requestMatchers(HttpMethod.POST, HISTORIAL_API_PATH)
                        .hasRole(VETERINARIO_ROLE)

                        // PUT → Veterinario y Admin
                        .requestMatchers(HttpMethod.PUT, HISTORIAL_API_PATH)
                        .hasAnyRole(VETERINARIO_ROLE, ADMIN_ROLE)

                        // DELETE → Veterinario y Admin
                        .requestMatchers(HttpMethod.DELETE, HISTORIAL_API_PATH)
                        .hasAnyRole(VETERINARIO_ROLE, ADMIN_ROLE)

                        // (Regla 5): Cualquier otra petición debe estar, al menos, autenticada.
                        // Cualquier otra request requiere autenticación
                        .anyRequest().authenticated());

        return http.build();
    }
    /**
     * Convierte el claim "roles" del JWT en autoridades de Spring Security.
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