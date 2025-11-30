package co.cue.pedidos_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * Clase principal del microservicio de Pedidos.
 *
 * Responsabilidades:
 * ------------------
 * - Arranca la aplicación Spring Boot.
 * - Registra el servicio en el Discovery Server (Eureka),
 *   permitiendo que otros microservicios lo encuentren.
 *
 * Notas:
 * - @SpringBootApplication habilita componentes como:
 *      * @Configuration
 *      * @EnableAutoConfiguration
 *      * @ComponentScan
 *
 * - @EnableDiscoveryClient permite que este servicio
 *   se registre automáticamente en Eureka al iniciar.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PedidosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidosServiceApplication.class, args);
	}

}
