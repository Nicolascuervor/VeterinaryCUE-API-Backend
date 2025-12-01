package co.cue.agendamiento_service.config;

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

    // Rutas base leídas desde properties (o hardcoded si prefieres simplificar)
    @Value("${api.endpoints.servicios-admin-path}")
    private String serviciosAdminPath;

    // NOTA: El path base cambió de ".../disponibilidad" a ".../agendamiento" en el nuevo controlador
    // Puedes actualizar tu application.properties o usar esta constante:
    private static final String AGENDAMIENTO_PATH = "/api/agendamiento";

    // Roles
    private static final String ADMIN = "ADMIN";
    private static final String VETERINARIO = "VETERINARIO";

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        http.authorizeHttpRequests(authz -> authz
                // -----------------------------------------------------------
                // 1. SERVICIOS ADMINISTRATIVOS (CRUD de Servicios Médicos)
                // -----------------------------------------------------------
                // Ver lista de servicios (Público o Autenticado)
                .requestMatchers(HttpMethod.GET, serviciosAdminPath + "/**").authenticated()

                // Crear servicios: Admin o Veterinario (según tu regla original)
                .requestMatchers(HttpMethod.POST, serviciosAdminPath + "/**").hasAnyRole(ADMIN, VETERINARIO)

                // Editar/Borrar servicios: Solo Admin
                .requestMatchers(HttpMethod.PUT, serviciosAdminPath + "/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, serviciosAdminPath + "/**").hasRole(ADMIN)

                // -----------------------------------------------------------
                // 2. MÓDULO DE AGENDAMIENTO (Nueva Arquitectura)
                // -----------------------------------------------------------

                // Configurar Jornada Laboral (El "Lienzo"): Solo Admin o el propio Veterinario
                .requestMatchers(HttpMethod.POST, AGENDAMIENTO_PATH + "/jornada").hasAnyRole(ADMIN, VETERINARIO)

                // Crear Bloqueo Manual ("Tengo una reunión"): Solo Admin o Veterinario
                .requestMatchers(HttpMethod.POST, AGENDAMIENTO_PATH + "/bloqueo").hasAnyRole(ADMIN, VETERINARIO)

                // Consultar Calendario Visual: ¡Abierto a todos los autenticados!
                // (Un dueño necesita ver el calendario para elegir cita)
                .requestMatchers(HttpMethod.GET, AGENDAMIENTO_PATH + "/calendario/**").authenticated()

                // -----------------------------------------------------------
                // 3. API INTERNA (Llamadas S2S desde Citas-Service)
                // -----------------------------------------------------------
                // Estos endpoints son técnicos. Idealmente se protegen con IP o Token de Servicio,
                // pero por ahora basta con que el usuario tenga un token válido.
                .requestMatchers(AGENDAMIENTO_PATH + "/interno/**").authenticated()

                // -----------------------------------------------------------
                // 4. Regla por Defecto
                // -----------------------------------------------------------
                .anyRequest().authenticated()
        );

        return http.build();
    }

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