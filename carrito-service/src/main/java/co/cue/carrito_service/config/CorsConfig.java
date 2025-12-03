package co.cue.carrito_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración global de CORS para el servicio de carrito.
 * Permite peticiones desde cualquier origen.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir cualquier origen
        config.setAllowedOriginPatterns(List.of("*"));
        
        // Permitir todos los métodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Permitir todos los headers
        config.setAllowedHeaders(List.of("*"));
        
        // Permitir credenciales
        config.setAllowCredentials(true);
        
        // Exponer headers en la respuesta
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Aplicar a todas las rutas
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

