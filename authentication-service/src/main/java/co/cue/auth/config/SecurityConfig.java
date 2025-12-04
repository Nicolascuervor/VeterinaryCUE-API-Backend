package co.cue.auth.config;

import co.cue.auth.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


/**
 * Configuración central de seguridad para el servicio de Autenticación.
 * Esta clase define los componentes fundamentales de Spring Security:
 * Codificación de contraseñas.
 * Gestión de autenticación.
 * Validación de tokens JWT para endpoints protegidos.
 * Reglas de autorización HTTP (quién puede acceder a qué ruta).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita anotaciones como @PreAuthorize en métodos
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String VETERINARIO_ROLE = "VETERINARIO";
    private static final String DUENIO_ROLE = "DUENIO";
    private final String secretKey;

    /**
     * Constructor para inyección de dependencias y propiedades.
     */
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          @Value("${jwt.secret.key}") String secretKey) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
    }

    /**
     * Define el codificador de contraseñas de la aplicación.
     * Utilizamos BCrypt, que es el estándar actual de la industria. Aplica un hash seguro
     * con "salt" aleatorio, haciendo que las contraseñas sean irreversibles y seguras
     * contra ataques de diccionario o tablas arcoíris.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el proveedor de autenticación (AuthenticationProvider).
     * Este componente es el encargado de verificar la identidad del usuario.
     * Conecta el UserDetailsServiceImpl para buscar el usuario con el
     * PasswordEncoder para verificar la contraseña ingresada.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expone el AuthenticationManager como un Bean.
     * El AuthenticationManager es el coordinador principal que delega la validación
     * al AuthenticationProvider adecuado. Lo necesitamos inyectar en el controlador
     * de Login para procesar las credenciales.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura el decodificador de JWT.
     * Crea un decodificador capaz de validar la firma de los tokens entrantes
     * utilizando la misma clave secreta simétrica (HMAC) con la que fueron firmados.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /**
     * Configura el convertidor de autenticación JWT.
     * Este bean extrae los roles (authorities) del token JWT y los convierte en objetos
     * que Spring Security entiende. Por defecto, Spring espera el prefijo "ROLE_",
     * pero aquí lo configuramos para que acepte los roles tal cual vienen en el token (prefijo vacío).
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Nombre del campo en el JSON del token
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // Sin prefijo adicional

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }

    /**
     * Define la cadena de filtros de seguridad (SecurityFilterChain).
     * Aquí se establecen las reglas de alto nivel para las peticiones HTTP:
     * 1. Deshabilitar CSRF (no necesario para API stateless).
     * 2. Establecer política de sesión STATELESS (sin cookies de sesión, cada petición es independiente).
     * 3. Configurar el servidor de recursos OAuth2/JWT.
     * 4. Definir la matriz de autorización (rutas públicas y privadas).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque usamos JWT y no cookies de sesión de navegador
                .csrf(AbstractHttpConfigurer::disable)

                // CORS se maneja en el API Gateway, no aquí para evitar duplicación de headers
                .cors(AbstractHttpConfigurer::disable)

                // Configuramos la gestión de sesiones como STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuramos este servicio como un Resource Server que acepta JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                // Reglas de Autorización por Ruta
                .authorizeHttpRequests(authz -> authz
                        // Rutas Públicas de Registro inicial y Login
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        // Endpoint público para obtener información básica de usuario (para confirmación de citas)
                        .requestMatchers(HttpMethod.GET, "/api/auth/public/**").permitAll()

                        //Rutas Administrativas (Solo ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/auth/active/users").hasAnyRole(ADMIN_ROLE, VETERINARIO_ROLE, DUENIO_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/**").hasRole(ADMIN_ROLE)

                        // Rutas Autenticadas (Perfil propio)
                        // Cualquier usuario autenticado (Dueño, Vet, Admin) puede ver/editar su perfil
                        .requestMatchers(HttpMethod.GET, "/api/auth/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/auth/email").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/auth/{id}").authenticated()

                        .requestMatchers("/api/auth/uploads/**").permitAll()
                        .anyRequest().authenticated()

                );

        return http.build();
    }
}
