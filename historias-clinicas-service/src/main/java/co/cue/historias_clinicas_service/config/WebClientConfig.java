package co.cue.historias_clinicas_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * Builder de WebClient con balanceo de carga habilitado.
     * Permite llamar a otros microservicios usando su nombre lógico
     * registrado en Eureka (por ejemplo: "http://mascotas-service").
     */

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}