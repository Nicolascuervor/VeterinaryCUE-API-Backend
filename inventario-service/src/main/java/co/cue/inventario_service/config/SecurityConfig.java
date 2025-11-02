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

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String INVENTARIO_API_PATH = "/api/inventario/**";

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Bean
    public JwtDecoder jwtDecoder() {
        // Decodifica la clave secreta desde Base64
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        http
                .authorizeHttpRequests(authz -> authz
                        // REGLA 1 (Pública): GET es público
                        .requestMatchers(HttpMethod.GET, INVENTARIO_API_PATH).permitAll()

                        // REGLA 2 (Admin): POST requiere rol ADMIN
                        .requestMatchers(HttpMethod.POST, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)

                        // REGLA 3 (Admin): PUT requiere rol ADMIN
                        .requestMatchers(HttpMethod.PUT, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)

                        // REGLA 4 (Admin): DELETE requiere rol ADMIN
                        .requestMatchers(HttpMethod.DELETE, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)

                        // REGLA 5 (Fallback): Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated());

        return http.build();
    }
}