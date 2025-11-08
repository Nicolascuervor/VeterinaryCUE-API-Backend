package co.cue.carrito_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class CarritoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarritoServiceApplication.class, args);
	}

}
