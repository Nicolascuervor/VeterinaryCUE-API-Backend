package co.cue.facturas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FacturasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacturasServiceApplication.class, args);
	}

}
