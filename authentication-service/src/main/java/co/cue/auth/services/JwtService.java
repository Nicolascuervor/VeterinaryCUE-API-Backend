package co.cue.auth.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import co.cue.auth.models.entities.Usuario;


/**
 * Servicio encargado de la generación y gestión de JSON Web Tokens (JWT).
 * Esta clase es responsable de crear las credenciales de acceso (tokens)
 * una vez que un usuario ha sido autenticado correctamente.
 * Actúa como el "Emisor" (Issuer) de la seguridad en el sistema. Los tokens generados aquí
 * son firmados digitalmente y contienen la información (Claims) necesaria para que
 * el API Gateway y otros microservicios puedan identificar al usuario y sus permisos
 * de manera stateless (sin sesión en el servidor).
 */
@Service
public class JwtService {

    /**
     * Clave secreta para la firma digital de los tokens.
     * Se inyecta desde la configuración y debe ser lo suficientemente robusta (256 bits)
     * para el algoritmo HMAC.
     */
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * Genera un nuevo token JWT para un usuario autenticado.
     *
     * Este método orquesta la construcción del payload del token.
     * Se encarga de extraer la información relevante del principal (UserDetails)
     * y convertirla en "Claims" estándar y personalizados.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Inyección de Roles
        // Extraemos los roles de Spring Security y los añadimos como una lista de Strings.
        // Esto permite al Gateway y Resource Servers validar autorización (hasRole) sin ir a la BD.
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        List<String> roleNames = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roleNames);

        // Inyección de Identidad Personalizada
        // Si el principal es nuestra entidad Usuario, extraemos su ID numérico.
        // Este ID (usuarioId) es vital para operaciones de negocio (ej. buscar carrito, crear pedido).
        if (userDetails instanceof Usuario usuario) {
            claims.put("usuarioId", usuario.getId());
        }

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Construye y firma el token JWT final.
     * Define las propiedades estándar del token (tiempos) y aplica la firma criptográfica.
     * Configuración actual:
     * - Subject: El correo del usuario (username).
     * - IssuedAt: Fecha y hora actual.
     * - Expiration: 10 horas desde la emisión.
     * - Algoritmo: HMAC SHA-256 (HS256).
     */
    private String createToken(Map<String, Object> claims, String subject) {

        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 Horas de validez

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Decodifica y prepara la clave criptográfica.
     * Convierte la clave secreta (almacenada en Base64 en properties) a un objeto Key
     * utilizable por el algoritmo de firma HMAC.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
