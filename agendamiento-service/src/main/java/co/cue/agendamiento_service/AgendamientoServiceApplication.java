package co.cue.agendamiento_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// Indica que esta clase es el punto de entrada de una aplicación Spring Boot.
// Activa el escaneo de componentes, configuración automática y más.
@SpringBootApplication

// Habilita el cliente de descubrimiento en Eureka, Consul u otro Discovery Server.
// Permite que este microservicio se registre y sea encontrado por otros.
@EnableDiscoveryClient
public class AgendamientoServiceApplication {
    // Método principal que inicia la aplicación.
    // Lanza la aplicación Spring Boot utilizando esta clase como configuración principal.
	public static void main(String[] args) {
		SpringApplication.run(AgendamientoServiceApplication.class, args);
	}

}
