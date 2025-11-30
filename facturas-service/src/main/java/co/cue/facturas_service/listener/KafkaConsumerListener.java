package co.cue.facturas_service.listener;

import co.cue.facturas_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.facturas_service.services.FacturaStrategyOrchestrator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
// Marca esta clase como un componente de Spring para que pueda ser detectada
// y gestionada automáticamente por el contenedor IoC.
@Component

// Anotación de Lombok que habilita un logger (log) sin necesidad de declararlo manualmente
@Slf4j

// Genera automáticamente un constructor con todos los atributos 'final'.
@AllArgsConstructor

public class KafkaConsumerListener {
    // Orquestador encargado de ejecutar la estrategia adecuada
    // cuando llega el evento de pedido completado.
    private final FacturaStrategyOrchestrator orchestrator;


    // LISTENER KAFKA: Escucha mensajes del tópico especificado
    @KafkaListener(topics = "pedidos_completados_topic",   // Tópico al que se suscribe el consumidor
            groupId = "${spring.kafka.consumer.group-id}")                // Grupo de consumidores definido en application.yml
    public void handlePedidoCompletado(PedidoCompletadoEventDTO evento) {

        // Log informativo indicando la recepción del evento y el ID del pedido.
        log.info("Evento 'PedidoCompletadoEventDTO' recibido en facturas-service para Pedido ID: {}", evento.getPedidoId());

        try {
            // Invoca al orquestador para procesar la generación de factura
            // según la lógica definida para este tipo de evento.
            orchestrator.procesarGeneracionFactura(evento);
        } catch (Exception e) {
            //Registra un error en caso de que algo falle durante el procesamiento.
            log.error("Error al procesar la generación de factura para Pedido ID: {}", evento.getPedidoId(), e);

        }
    }

}
