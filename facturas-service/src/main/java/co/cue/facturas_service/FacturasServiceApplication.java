package co.cue.facturas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FacturasServiceApplication {
    // Punto de entrada del microservicio de facturas.
    // Spring Boot inicializa el contexto, configura los beans
    // y registra el servicio en el servidor Eureka gracias a @EnableDiscoveryClient.
	public static void main(String[] args) {
		SpringApplication.run(FacturasServiceApplication.class, args);
	}

}
