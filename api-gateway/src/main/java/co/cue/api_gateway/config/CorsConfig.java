package co.cue.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;


/**
 * Configuración global de CORS (Cross-Origin Resource Sharing) para el API Gateway.
 * Esta clase define las políticas de seguridad que permiten o restringen las solicitudes HTTP
 * provenientes de orígenes diferentes al del servidor (ej. frontend en React o Angular).
 * Al configurar CORS en el Gateway, centralizamos esta política para todos los microservicios,
 * evitando tener que configurarlo individualmente en cada uno.
 */
@Configuration
public class CorsConfig {

    /**
     * Crea y registra un filtro web de CORS para el manejo reactivo (WebFlux).
     * Este bean intercepta todas las solicitudes entrantes y verifica si cumplen con las
     * reglas definidas (orígenes, métodos, encabezados). Si la solicitud es válida,
     * agrega los encabezados CORS necesarios a la respuesta; de lo contrario, el navegador
     * bloqueará la respuesta.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {

        // Instancia de la configuración de CORS donde definiremos las reglas.
        CorsConfiguration config = new CorsConfiguration();

        // Configuración de Orígenes Permitidos
        // Usamos setAllowedOriginPatterns con "*" para permitir cualquier origen
        // NOTA: setAllowedOriginPatterns permite usar "*" incluso con allowCredentials=true
        config.setAllowedOriginPatterns(List.of("*"));

        // Configuración de Métodos y Cabeceras
        // Permitimos todos los HTTP (GET, POST, PUT, DELETE, OPTIONS, etc.)
        config.addAllowedMethod("*");

        // Permitimos todas las cabeceras personalizadas (ej. Authorization, X-Usuario-Id, Content-Type)
        config.addAllowedHeader("*");

        // Credenciales
        // IMPORTANTE: Cuando allowCredentials es true, Spring Gateway manejará automáticamente
        // el origen específico en la respuesta usando setAllowedOriginPatterns("*")
        config.setAllowCredentials(true);
        
        // Tiempo máximo que el navegador puede cachear la respuesta preflight (OPTIONS)
        // 3600 segundos = 1 hora
        config.setMaxAge(3600L);

        // Registro de la Configuración
        // Asociamos la configuración creada a rutas específicas URL.
        // Usamos UrlBasedCorsConfigurationSource con PathPatternParser para un matcheo eficiente en WebFlux.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());

        // Aplicamos esta configuración a TODAS las rutas del sistema ("/**").
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}