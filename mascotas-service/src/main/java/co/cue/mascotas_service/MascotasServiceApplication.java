package co.cue.mascotas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// Marca la aplicación como un microservicio Spring Boot.
@SpringBootApplication

// Habilita el registro en un servidor Eureka u otro Discovery Service.
@EnableDiscoveryClient
public class MascotasServiceApplication {
	// Punto de entrada de la aplicación. Inicia el microservicio.
	public static void main(String[] args) {
		SpringApplication.run(MascotasServiceApplication.class, args);
	}
}