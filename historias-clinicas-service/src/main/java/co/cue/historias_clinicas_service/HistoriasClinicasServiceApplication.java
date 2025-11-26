package co.cue.historias_clinicas_service;
// Importa la clase principal de Spring Boot
import org.springframework.boot.SpringApplication;
// Habilita la configuración automática del proyecto
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Habilita el descubrimiento del servicio en Eureka u otro servicio de registro
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// Anotación que indica que esta es la clase principal de una aplicación Spring Boot
@SpringBootApplication

// Permite que este microservicio se registre en un servidor Eureka
@EnableDiscoveryClient
public class HistoriasClinicasServiceApplication {

	public static void main(String[] args) {
		// Método principal que inicia la aplicación Spring Boot
		SpringApplication.run(HistoriasClinicasServiceApplication.class, args);
	}

}
