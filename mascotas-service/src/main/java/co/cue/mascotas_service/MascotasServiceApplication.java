package co.cue.mascotas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MascotasServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MascotasServiceApplication.class, args);
	}
}