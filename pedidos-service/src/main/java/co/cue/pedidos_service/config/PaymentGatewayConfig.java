package co.cue.pedidos_service.config;

import co.cue.pedidos_service.pasarela.IPasarelaPagoGateway;
import co.cue.pedidos_service.pasarela.concreteadapters.SimulatedPaymentGateway;
import co.cue.pedidos_service.pasarela.concreteadapters.StripeAdapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * PaymentGatewayConfig
 *
 * Configuración para seleccionar el adaptador de pasarela de pagos.
 * Permite alternar entre Stripe real y simulación mediante una variable de entorno.
 *
 * Uso:
 * - Para usar Stripe real: payment.simulation.enabled=false (o no configurar)
 * - Para usar simulación: payment.simulation.enabled=true
 */
@Configuration
@Slf4j
public class PaymentGatewayConfig {

    /**
     * Bean para el adaptador simulado.
     * Solo se crea si payment.simulation.enabled=true
     */
    @Bean(name = "simulatedPaymentGateway")
    @ConditionalOnProperty(name = "payment.simulation.enabled", havingValue = "true")
    public SimulatedPaymentGateway simulatedPaymentGateway() {
        log.info("🔧 MODO SIMULACIÓN ACTIVADO: Creando SimulatedPaymentGateway");
        log.warn("⚠️  Los pagos serán simulados. No se realizarán transacciones reales.");
        return new SimulatedPaymentGateway();
    }

    /**
     * Bean primario que selecciona el adaptador correcto según la configuración.
     * Si la simulación está habilitada, usa el adaptador simulado.
     * Si no, usa Stripe real.
     */
    @Bean
    @Primary
    public IPasarelaPagoGateway paymentGateway(
            @Value("${payment.simulation.enabled:false}") boolean simulationEnabled,
            StripeAdapterService stripeAdapterService) {
        
        if (simulationEnabled) {
            log.info("✅ Configuración: Usando SimulatedPaymentGateway");
            log.warn("⚠️  MODO SIMULACIÓN: Los pagos no serán procesados por Stripe");
            return new SimulatedPaymentGateway();
        } else {
            log.info("✅ Configuración: Usando StripeAdapterService");
            return stripeAdapterService;
        }
    }
}

