package co.cue.citas_service.config;

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

    // Roles permitidos en el sistema
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String VETERINARIO_ROLE = "VETERINARIO";
    private static final String DUENIO_ROLE = "DUEÑO";

    // Ruta base del API de citas
    private static final String CITAS_API_PATH = "/api/citas/**";

    // Llave secreta para validar los JWT
    @Value("${jwt.secret.key}") //
    private String secretKey;

    // Decodificador JWT usando la llave secreta
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    // Configuración principal de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactiva CSRF porque usamos autenticación stateless
                .csrf(AbstractHttpConfigurer::disable)

                // No se guardan sesiones, cada petición se autentica con JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Indica que este servicio actuará como Resource Server con JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        http
                .authorizeHttpRequests(authz -> authz
                        // GET: Dueños, Vets y Admins pueden leer citas.
                        .requestMatchers(HttpMethod.GET, CITAS_API_PATH)
                        .hasAnyRole(DUENIO_ROLE, VETERINARIO_ROLE, ADMIN_ROLE)

                        // POST: Dueños, Vets y Admins pueden crear citas.
                        .requestMatchers(HttpMethod.POST, CITAS_API_PATH)
                        .hasAnyRole(DUENIO_ROLE, VETERINARIO_ROLE, ADMIN_ROLE)

                        // PUT: Solo Vets y Admins pueden actualizar (completar, añadir info médica).
                        .requestMatchers(HttpMethod.PUT, CITAS_API_PATH)
                        .hasAnyRole(VETERINARIO_ROLE, ADMIN_ROLE)

                        // DELETE: Dueños, Vets y Admins pueden cancelar (borrar) citas.
                        .requestMatchers(HttpMethod.DELETE, CITAS_API_PATH)
                        .hasAnyRole(DUENIO_ROLE, VETERINARIO_ROLE, ADMIN_ROLE)

                        .anyRequest().authenticated()
                );

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // No usamos "ROLE_"

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }
}