package co.cue.citas_service.config;

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

@Configuration
public class KafkaProducerConfig {

    // Obtiene la dirección del servidor Kafka desde application.yml
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Configura el productor de Kafka
    @Bean
    public ProducerFactory<String, Object> producerFactory() {

        // Mapa donde se guardan todas las propiedades de configuración
        Map<String, Object> configProps = new HashMap<>();

        // Dirección del servidor Kafka
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Serializador para la clave (String)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Serializador para el valor (JSON)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Evita agregar metadata del tipo de objeto en los headers
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        // Retorna la fábrica con la configuración del productor
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // KafkaTemplate permite enviar mensajes a los tópicos
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}