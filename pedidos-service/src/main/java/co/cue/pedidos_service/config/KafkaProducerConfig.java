package co.cue.pedidos_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
// Indica que esta clase contiene configuraciones de Spring
@Configuration
public class KafkaProducerConfig {
    // Inyecta el valor del servidor de Kafka desde application.properties
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Define un bean que configura y crea la fábrica de productores Kafka
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        // Mapa con las propiedades necesarias para configurar el productor
        Map<String, Object> configProps = new HashMap<>();
        // Dirección del servidor Kafka al cual conectarse
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Serializador usado para la clave (String)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Serializador para el valor, que convierte los objetos a JSON
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // Desactiva la inclusión automática de información de tipo en los headers
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        // Retorna una fábrica de productores con la configuración anterior
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // Define un bean KafkaTemplate para enviar mensajes a Kafka
    // Crea el KafkaTemplate utilizando el ProducerFactory configurado
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}