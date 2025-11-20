package co.cue.facturas_service.strategies.concretestrategie;

import co.cue.facturas_service.models.dtos.kafka.NotificationRequestDTO;
import co.cue.facturas_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.facturas_service.models.entities.FacturaProductos;
import co.cue.facturas_service.models.entities.LineaFactura;
import co.cue.facturas_service.models.enums.EstadoFactura;
import co.cue.facturas_service.models.enums.MetodoPago;
import co.cue.facturas_service.models.enums.NotificationType;
import co.cue.facturas_service.models.enums.TipoFactura;
import co.cue.facturas_service.repository.FacturaRepository;
import co.cue.facturas_service.services.KafkaNotificationProducer;
import co.cue.facturas_service.strategies.IFacturaGenerationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class ProductoFacturaStrategy implements IFacturaGenerationStrategy {

    private final FacturaRepository facturaRepository;
    private final KafkaNotificationProducer notificationProducer;


    @Override
    @Transactional
    public void generarFactura(Object eventPayload) {

        PedidoCompletadoEventDTO evento = (PedidoCompletadoEventDTO) eventPayload;
        log.info("Estrategia PRODUCTOS: Generando factura para Pedido ID: {}", evento.getPedidoId());

        if (facturaRepository.existsByIdOrigenAndTipoFactura(evento.getPedidoId(), getTipo())) {
            log.warn("Ya existe una factura para el Pedido ID: {}. Ignorando evento.", evento.getPedidoId());
            return;
        }

        Set<LineaFactura> lineas = evento.getItems().stream()
                .map(itemDTO -> {
                    LineaFactura linea = new LineaFactura();
                    linea.setProductoId(itemDTO.getProductoId());
                    linea.setCantidad(itemDTO.getCantidad());
                    linea.setPrecioUnitarioVenta(itemDTO.getPrecioUnitario());
                    linea.setSubtotalLinea(
                            itemDTO.getPrecioUnitario().multiply(new BigDecimal(itemDTO.getCantidad()))
                    );
                    return linea;
                }).collect(Collectors.toSet());


        FacturaProductos factura = new FacturaProductos();
        factura.setIdOrigen(evento.getPedidoId());
        factura.setUsuarioId(evento.getUsuarioId());


        factura.setNumFactura("F-PROD-" + evento.getPedidoId());

        factura.setFechaEmision(evento.getFechaCreacion().toLocalDate());
        factura.setEstadoFactura(EstadoFactura.PAGADA);
        factura.setMetodoPago(MetodoPago.TARJETA_CREDITO); // Asumimos Stripe

        factura.setTotal(evento.getTotalPedido());
        factura.setSubTotal(evento.getTotalPedido());
        factura.setImpuestos(BigDecimal.ZERO);

        // --- 3. Asociar y Guardar ---
        for (LineaFactura linea : lineas) {
            linea.setFactura(factura); // Asociamos la línea al padre
        }
        factura.setLineas(lineas);

        facturaRepository.save(factura);
        log.info("Factura {} creada exitosamente.", factura.getNumFactura());
        enviarNotificacionFactura(evento, factura);
    }

    private void enviarNotificacionFactura(PedidoCompletadoEventDTO evento, FacturaProductos factura) {
        Map<String, String> payload = new HashMap<>();
        payload.put("clienteNombre", evento.getClienteNombre());
        payload.put("clienteEmail", evento.getClienteEmail());
        payload.put("numFactura", factura.getNumFactura());
        payload.put("total", factura.getTotal().toString());
        payload.put("fecha", factura.getFechaEmision().format(DateTimeFormatter.ISO_DATE));

        NotificationRequestDTO notificacion = new NotificationRequestDTO(
                NotificationType.FACTURA,
                payload
        );

        // Enviamos
        notificationProducer.enviarNotificacion(notificacion);
    }

    @Override
    public TipoFactura getTipo() {
        return TipoFactura.PRODUCTOS;
    }
}
