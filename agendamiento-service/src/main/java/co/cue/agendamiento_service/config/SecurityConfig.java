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
// Marca la clase como una fuente de configuraciones de Spring (component-scan + configuración)
@Configuration
// Habilita la seguridad web de Spring (filtros, autenticación/autorization HTTP)

// Permite el uso de anotaciones de seguridad en métodos como @PreAuthorize y @RolesAllowed
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Inyecta desde application.properties/yml la ruta base para endpoints administrativos
    @Value("${api.endpoints.servicios-admin-path}")
    private String serviciosAdminPath;

    // Inyecta desde configuración la ruta base para endpoints de disponibilidad (veterinaria)
    @Value("${api.endpoints.disponibilidad-path}")
    private String disponibilidadPath;

    // Constante que representa el rol de administrador
    private static final String ADMIN = "ADMIN";
    // Constante para rol de veterinario
    private static final String VETERINARIO = "VETERINARIO";
    // Constante para rol de dueño de mascota
    private static final String DUENIO_MASCOTA = "DUENIO";

    // Inyecta la clave secreta JWT codificada en Base64 desde configuration (se usa para validar tokens)
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * Bean que construye el JwtDecoder usado por Spring Security para validar tokens JWT.
     *  - Decodifica la clave desde Base64.
     *  - Construye una SecretKey compatible con HMAC-SHA256 (HS256).
     *  - Devuelve un NimbusJwtDecoder configurado con esa clave.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        // Decodifica la clave secreta que está en Base64 en la configuración
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        // Crea una clave secreta HmacSHA256 a partir de los bytes decodificados
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        // Construye y retorna el JwtDecoder que Spring usará para validar firmas y claims
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /**
     * Bean principal que configura la cadena de filtros de seguridad (SecurityFilterChain).
     * Define CSRF, sesión (stateless), resource server JWT y reglas de autorización por ruta/método.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Desactiva CSRF (válido para APIs stateless que usan Authorization: Bearer)
        http
                .csrf(AbstractHttpConfigurer::disable)
                // Configura que la aplicación no mantiene sesión en servidor (cada petición debe llevar token)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura esta app como un OAuth2 Resource Server que valida JWTs
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                // Indica cómo convertir el JWT a Authentication (roles/authorities)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );
        // Configuración de autorizaciones por rutas y métodos HTTP
        http
                .authorizeHttpRequests(authz -> authz
                        // Requiere autenticación para cualquier GET sobre serviciosAdminPath/**
                        .requestMatchers(HttpMethod.GET, serviciosAdminPath + "/**").authenticated()

                        // POST a serviciosAdminPath/** requiere rol ADMIN o DUENIO_MASCOTA
                        .requestMatchers(HttpMethod.POST, serviciosAdminPath + "/**").hasAnyRole(ADMIN, DUENIO_MASCOTA)

                        // PUT a serviciosAdminPath/** solo ADMIN
                        .requestMatchers(HttpMethod.PUT, serviciosAdminPath + "/**").hasAnyRole(ADMIN)

                        // DELETE a serviciosAdminPath/** solo ADMIN
                        .requestMatchers(HttpMethod.DELETE, serviciosAdminPath + "/**").hasRole(ADMIN)

                        // POST a disponibilidad/generar-slots solo ADMIN
                        .requestMatchers(HttpMethod.POST, disponibilidadPath + "/generar-slots").hasRole(ADMIN)

                        // POST a disponibilidad/jornada ADMIN o VETERINARIO
                        .requestMatchers(HttpMethod.POST, disponibilidadPath + "/jornada").hasAnyRole(ADMIN, VETERINARIO)

                        // PATCH sobre disponibilidad/slots/** ADMIN o VETERINARIO
                        .requestMatchers(HttpMethod.PATCH, disponibilidadPath + "/slots/**").hasAnyRole(ADMIN, VETERINARIO)

                        // Cualquier otra ruta bajo disponibilidadPath/** requiere autenticación (pero no roles específicos)
                        .requestMatchers(disponibilidadPath + "/**").authenticated()

                        // Todas las demás peticiones requieren autenticación por defecto
                        .anyRequest().authenticated()
                );
// Construye y retorna la cadena de filtros con toda la configuración anterior
        return http.build();
    }

    /**
     * Bean que convierte los claims "roles" del JWT en GrantedAuthorities de Spring.
     *
     * Detalles:
     * - Se lee el claim 'roles' del JWT.
     * - Se elimina el prefijo por defecto "ROLE_" para poder usar hasRole("ADMIN") directamente.
     * - Devuelve un JwtAuthenticationConverter que Spring Security usará para poblar Authentication.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Conversor que extrae las autoridades a partir de claims del JWT
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Indica que los roles están en el claim "roles" (ej. "roles": ["ADMIN","VETERINARIO"])
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        // Elimina el prefijo "ROLE_" que Spring añade por defecto; así se usan roles tal como vienen
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        // Conversor final que transformará el JWT en Authentication con las GrantedAuthorities extraídas
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        // Retorna el conversor configurado
        return jwtConverter;
    }
}