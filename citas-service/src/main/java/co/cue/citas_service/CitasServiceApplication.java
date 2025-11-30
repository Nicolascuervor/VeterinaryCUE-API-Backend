package co.cue.citas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

// Clase principal para ejecutar el microservicio de Citas
public class CitasServiceApplication {

	// Método main para iniciar la aplicación
	public static void main(String[] args) {
		SpringApplication.run(CitasServiceApplication.class, args);
	}

}
