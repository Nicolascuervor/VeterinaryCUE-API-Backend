package co.cue.facturas_service.config;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;
// Indica que esta clase contiene configuraciones de Spring
@Configuration

// Activa la seguridad web de Spring Security.
@EnableWebSecurity

// Habilita la seguridad basada en anotaciones @PreAuthorize, @Secured, etc.
@EnableMethodSecurity
public class SecurityConfig {

    // Constante que representa el rol ADMIN.
    private static final String ADMIN_ROLE = "ADMIN";
    // Constante para el rol VETERINARIO.
    private static final String VETERINARIO_ROLE = "VETERINARIO";
    // Constante para el rol DUEÑO.
    private static final String DUENIO_ROLE = "DUENIO";

    // Ruta base protegida para las APIs de facturas.
    private static final String FACTURAS_API_PATH = "/api/facturas/**";

    // Clave secreta para validar los JWT, obtenida del archivo de configuración.
    @Value("${jwt.secret.key}")
    private String secretKey;


    // CONFIGURACIÓN DEL DECODER JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        // Decodifica la clave en Base64.
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        // Crea un SecretKey usando el algoritmo HMAC-SHA256.
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        // Construye el decodificador Nimbus con la clave especificada.
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    // CONFIGURACIÓN DE LA CADENA DE SEGURIDAD
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilita CSRF ya que este es un servicio stateless (sin sesiones).
                .csrf(AbstractHttpConfigurer::disable)

                // Habilita CORS con la configuración personalizada
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Indica que no se usará sesión; todas las peticiones deben incluir JWT.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Habilita el servidor de recursos OAuth2 y especifica que usará JWT.
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt

                                // Indica cómo convertir los claims del JWT a roles/authorities.
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        // CONFIGURACIÓN DE AUTORIZACIONES SEGÚN ROLES Y RUTAS
        http
                .authorizeHttpRequests(authz -> authz

                        // Permite SOLO a ADMIN consultar la lista general de facturas.
                        .requestMatchers(HttpMethod.GET, "/api/facturas").hasRole(ADMIN_ROLE)

                        // Permite acceso a facturas específicas a ADMIN, VETERINARIO y DUEÑO
                        .requestMatchers(HttpMethod.GET, FACTURAS_API_PATH)
                        .hasAnyRole(ADMIN_ROLE, VETERINARIO_ROLE, DUENIO_ROLE)

                        // Cualquier otra petición debe estar autenticada (tener JWT válido).
                        .anyRequest().authenticated()
                );

        // Construye la cadena de filtros de seguridad.
        return http.build();
    }


    // CONFIGURACIÓN DEL CONVERTIDOR JWT
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        // Convierte los claims "roles" en autoridades de Spring Security.
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Indica que los roles vienen en el claim llamado "roles"
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        // No agregar prefijo "ROLE_", ya vienen completos desde el JWT.
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        // Crea el convertidor general.
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

        // Asigna el convertidor de autoridades al convertidor principal.
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        // Retorna el convertidor final.
        return jwtConverter;
    }
    
    /**
     * Configuración de CORS para Spring Security.
     * Permite peticiones desde cualquier origen.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
