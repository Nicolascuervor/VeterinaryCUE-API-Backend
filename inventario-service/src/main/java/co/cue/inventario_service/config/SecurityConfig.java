package co.cue.inventario_service.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String INVENTARIO_API_PATH = "/api/inventario/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        http
                .authorizeHttpRequests(authz -> authz
                        // REGLA 1 (Pública):
                        .requestMatchers(HttpMethod.GET, INVENTARIO_API_PATH).permitAll()

                        // REGLA 2 (Admin):
                        .requestMatchers(HttpMethod.POST, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)

                        // REGLA 3 (Admin):
                        .requestMatchers(HttpMethod.PUT, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)

                        // REGLA 4 (Admin):
                        .requestMatchers(HttpMethod.DELETE, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)

                        // REGLA 5 (Fallback):
                        .anyRequest().authenticated());

        return http.build();
    }
}
