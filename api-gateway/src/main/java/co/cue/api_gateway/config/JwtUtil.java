package co.cue.api_gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


/**
 * Utilidad para el manejo y procesamiento de JSON Web Tokens (JWT).
 * Esta clase encapsula toda la lógica criptográfica necesaria para:
 * Verificar la firma digital e integridad de los tokens entrantes.
 * Decodificar el payload (claims) de los tokens.
 * Extraer información específica (como el ID del usuario) para su uso en el Gateway.
 * Se utiliza principalmente dentro del AuthenticationFilter para validar las sesiones
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar y verificar los tokens.
     * Se inyecta desde la configuración externa (application.properties/yml).
     * Debe coincidir exactamente con la clave usada por el servicio de autenticación para firmar.
     */
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * Verifica si un token JWT es válido y confiable.
     * Este método intenta parsear el token utilizando la clave secreta. Si el token ha sido alterado,
     * ha expirado o tiene un formato incorrecto, el parser lanzará una excepción, lo que indica
     * que el token no es válido.
     */
    public boolean isTokenValid(String token) {
        try {
            //  Validación de Firma y Expiración
            // El método parseSignedClaims verifica automáticamente:
            // 1. Que la firma criptográfica coincida con nuestra clave secreta.
            // 2. Que la fecha de expiración ('exp') no haya pasado.
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae todos los "claims" (datos) contenidos en el payload del token.
     * Este método es el punto central de decodificación. Parsea el token validado
     * y devuelve el objeto Claims que funciona como un mapa de los datos.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Requerido para asegurar que leemos datos confiables
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Genera la clave criptográfica (SecretKey) a partir de la cadena de configuración.
     * Convierte la clave secreta codificada en Base64 a un objeto SecretKey compatible
     * con el algoritmo HMAC-SHA (usualmente HS256 o superior).
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae específicamente el ID del usuario (Claim personalizado 'usuarioId').
     * Este dato es crítico para la arquitectura, ya que se inyectará en las cabeceras
     * HTTP para que los microservicios sepan quién está realizando la petición.
     */
    public Long extractUsuarioId(String token) {
        // Accedemos directamente al claim personalizado definido en el auth-service
        return extractAllClaims(token).get("usuarioId", Long.class);
    }
}