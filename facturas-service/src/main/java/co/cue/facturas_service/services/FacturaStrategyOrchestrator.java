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

    private final Map<TipoFactura, IFacturaGenerationStrategy> strategyMap;

    @Autowired
    public FacturaStrategyOrchestrator(List<IFacturaGenerationStrategy> strategies) {
        strategyMap = new EnumMap<>(TipoFactura.class);
        strategies.forEach(
                strategy -> strategyMap.put(strategy.getTipo(), strategy)
        );
        log.info("Estrategias de facturación cargadas: {}", strategyMap.keySet());
    }


    public void procesarGeneracionFactura(PedidoCompletadoEventDTO evento) {
        TipoFactura tipo = TipoFactura.PRODUCTOS;

        IFacturaGenerationStrategy strategy = strategyMap.get(tipo);

        if (strategy == null) {
            log.error("No se encontró estrategia de facturación para el tipo: {}", tipo);
            return;
        }
        strategy.generarFactura(evento);
    }
}
