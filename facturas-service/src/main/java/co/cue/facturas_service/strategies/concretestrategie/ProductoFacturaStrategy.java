package co.cue.facturas_service.strategies.concretestrategie;

import co.cue.facturas_service.models.dtos.kafka.NotificationRequestDTO;
import co.cue.facturas_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.facturas_service.models.entities.FacturaProductos;
import co.cue.facturas_service.models.entities.LineaFactura;
import co.cue.facturas_service.models.enums.EstadoFactura;
import co.cue.facturas_service.models.enums.MetodoPago;
import co.cue.facturas_service.models.enums.NotificationType;
import co.cue.facturas_service.models.enums.TipoFactura;
import co.cue.facturas_service.pattens.composite.FacturaCompositeBuilder;
import co.cue.facturas_service.pattens.composite.ItemFactura;
import co.cue.facturas_service.repository.FacturaRepository;
import co.cue.facturas_service.services.KafkaNotificationProducer;
import co.cue.facturas_service.strategies.IFacturaGenerationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class ProductoFacturaStrategy implements IFacturaGenerationStrategy {

    private final FacturaRepository facturaRepository;
    private final KafkaNotificationProducer notificationProducer;
    private final FacturaCompositeBuilder compositeBuilder;


    @Override
    @Transactional
    public void generarFactura(Object eventPayload) {
        PedidoCompletadoEventDTO evento = (PedidoCompletadoEventDTO) eventPayload;
        log.info("Estrategia PRODUCTOS: Procesando Pedido ID: {}", evento.getPedidoId());

        if (facturaRepository.existsByIdOrigenAndTipoFactura(evento.getPedidoId(), getTipo())) {
            log.warn("Factura ya existe para Pedido ID: {}. Omitiendo.", evento.getPedidoId());
            return;
        }
        FacturaProductos factura = new FacturaProductos();
        factura.setIdOrigen(evento.getPedidoId());
        factura.setUsuarioId(evento.getUsuarioId());

        factura.setNumFactura("F-PROD-" + evento.getPedidoId() + "-" + System.currentTimeMillis());
        factura.setFechaEmision(evento.getFechaCreacion().toLocalDate());
        factura.setEstadoFactura(EstadoFactura.PAGADA);
        factura.setMetodoPago(MetodoPago.TARJETA_CREDITO);
        factura.setTotal(evento.getTotalPedido());
        factura.setSubTotal(evento.getTotalPedido());
        factura.setImpuestos(BigDecimal.ZERO);
        Set<LineaFactura> lineas = evento.getItems().stream()
                .map(itemDTO -> {
                    LineaFactura linea = new LineaFactura();
                    linea.setProductoId(itemDTO.getProductoId());
                    linea.setCantidad(itemDTO.getCantidad());
                    linea.setPrecioUnitarioVenta(itemDTO.getPrecioUnitario());
                    linea.setSubtotalLinea(itemDTO.getPrecioUnitario().multiply(new BigDecimal(itemDTO.getCantidad())));
                    linea.setFactura(factura);
                    return linea;
                }).collect(Collectors.toSet());
        factura.setLineas(lineas);
        facturaRepository.save(factura);
        log.info("Factura {} guardada exitosamente en BD.", factura.getNumFactura());
        List<ItemFactura> estructuraFactura = compositeBuilder.construirEstructura(evento.getItems());
        StringBuilder detalleCorreo = new StringBuilder();
        for (ItemFactura item : estructuraFactura) {
            detalleCorreo.append(item.generarResumen(0));
        }
        enviarNotificacionFactura(evento, factura, detalleCorreo.toString());
    }

    private void enviarNotificacionFactura(PedidoCompletadoEventDTO evento, FacturaProductos factura, String detalleItems) {
        Map<String, String> payload = new HashMap<>();
        payload.put("clienteNombre", evento.getClienteNombre());
        payload.put("clienteEmail", evento.getClienteEmail());
        payload.put("numFactura", factura.getNumFactura());
        payload.put("total", factura.getTotal().toString());
        payload.put("fecha", factura.getFechaEmision().format(DateTimeFormatter.ISO_DATE));
        payload.put("detalleItems", detalleItems);
        NotificationRequestDTO notificacion = new NotificationRequestDTO(
                NotificationType.FACTURA,
                payload
        );

        notificationProducer.enviarNotificacion(notificacion);
        log.info("Evento de notificación enviado para factura {}", factura.getNumFactura());
    }

    @Override
    public TipoFactura getTipo() {
        return TipoFactura.PRODUCTOS;
    }
}
