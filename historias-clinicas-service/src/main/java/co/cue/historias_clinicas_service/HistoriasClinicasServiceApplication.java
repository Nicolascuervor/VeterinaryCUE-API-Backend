package co.cue.historias_clinicas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HistoriasClinicasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoriasClinicasServiceApplication.class, args);
	}

}
