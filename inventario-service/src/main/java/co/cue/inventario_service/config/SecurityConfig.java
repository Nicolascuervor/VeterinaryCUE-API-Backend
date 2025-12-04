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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration // Indica que esta es una clase de configuración de Spring
@EnableWebSecurity // Habilita la seguridad web
@EnableMethodSecurity  // Permite usar anotaciones como @PreAuthorize
public class SecurityConfig {

    // Rol que se requiere para operaciones administrativas
    private static final String ADMIN_ROLE = "ADMIN";

    // Ruta base de la API de inventario
    private static final String INVENTARIO_API_PATH = "/api/inventario/**";

    // Se inyecta la llave secreta del JWT desde application.properties
    @Value("${jwt.secret.key}")
    private String secretKey;

    // Decoder para verificar y decodificar los JWT usando la llave secreta
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
                // Desactiva CSRF porque se usa autenticación con tokens (stateless)
                .csrf(csrf -> csrf.disable())

                // CORS se maneja en el API Gateway, no aquí para evitar duplicación de headers
                .cors(cors -> cors.disable())

                // Define que no habrá sesiones, cada request es independiente
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura este servicio como servidor de recursos protegido con JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Aplicamos nuestro conversor
                        )
                );

        http
                // Reglas de autorización para cada endpoint según método HTTP
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/inventario/productos/stock/descontar").permitAll()
                        .requestMatchers(HttpMethod.GET, INVENTARIO_API_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, INVENTARIO_API_PATH).hasRole(ADMIN_ROLE)
                        .anyRequest().authenticated());
        return http.build();
    }

    // Configura cómo se obtienen los roles desde el token JWT
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