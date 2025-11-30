package co.cue.facturas_service.services;

import co.cue.facturas_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.facturas_service.models.enums.TipoFactura;
import co.cue.facturas_service.strategies.IFacturaGenerationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class FacturaStrategyOrchestrator {
    // Mapa que relaciona cada tipo de factura con su estrategia correspondiente.
    // Se usa EnumMap porque es más eficiente cuando la clave es un enum.
    private final Map<TipoFactura, IFacturaGenerationStrategy> strategyMap;

    @Autowired
    public FacturaStrategyOrchestrator(List<IFacturaGenerationStrategy> strategies) {

        // Se inicializa el mapa donde se guardarán las estrategias por tipo.
        strategyMap = new EnumMap<>(TipoFactura.class);


        // Spring inyecta todas las clases que implementan IFacturaGenerationStrategy.
        // Aquí recorremos cada estrategia y la registramos en el mapa
        // según el tipo de factura que maneja.
        strategies.forEach(
                strategy -> strategyMap.put(strategy.getTipo(), strategy)
        );

        // Log para verificar qué estrategias quedaron cargadas.
        log.info("Estrategias de facturación cargadas: {}", strategyMap.keySet());
    }


    public void procesarGeneracionFactura(PedidoCompletadoEventDTO evento) {

        // Tipo de factura que se va a procesar.
        // En este caso está fijo en PRODUCTOS,
        // pero se podría obtener dinámicamente desde el evento.
        TipoFactura tipo = TipoFactura.PRODUCTOS;

        // Se obtiene la estrategia asociada al tipo.
        IFacturaGenerationStrategy strategy = strategyMap.get(tipo);


        // Si no existe estrategia registrada para ese tipo, se reporta el error.
        if (strategy == null) {
            log.error("No se encontró estrategia de facturación para el tipo: {}", tipo);
            return;
        }

        // Se ejecuta la estrategia específica para generar la factura.
        strategy.generarFactura(evento);
    }
}
