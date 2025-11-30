package co.cue.pedidos_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
// Indica que esta clase contiene configuraciones de Spring
@Configuration
public class WebClientConfig {
    // Define un bean de tipo WebClient.Builder
    @Bean
    // Indica que el WebClient usará balanceo de carga (Netflix Ribbon o Spring Cloud LoadBalancer)
    // Construye y retorna un WebClient.Builder básico
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
