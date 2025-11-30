package co.cue.citas_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Crea un WebClient.Builder que soporta balanceo de carga entre microservicios
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        // Retorna el builder que usarán los clientes para hacer llamadas HTTP
        return WebClient.builder();
    }
}
