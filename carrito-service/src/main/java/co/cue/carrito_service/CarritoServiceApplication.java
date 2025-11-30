package co.cue.carrito_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// Indica que esta es una aplicación de Spring Boot
@SpringBootApplication
// Habilita la capacidad de registrarse en un servicio de descubrimiento (Eureka, Consul, etc.)
@EnableDiscoveryClient
public class CarritoServiceApplication {

	// Método principal que arranca la aplicación Spring Boot
	public static void main(String[] args) {

		SpringApplication.run(CarritoServiceApplication.class, args);
	}

}
