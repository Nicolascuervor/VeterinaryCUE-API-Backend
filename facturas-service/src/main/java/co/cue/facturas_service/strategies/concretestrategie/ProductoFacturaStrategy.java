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

    // Repositorio para guardar y consultar facturas en la base de datos
    private final FacturaRepository facturaRepository;

    // Productor Kafka para enviar notificaciones al microservicio de usuarios/notificaciones
    private final KafkaNotificationProducer notificationProducer;

    // Builder que arma la estructura compuesta de la factura (Composite)
    private final FacturaCompositeBuilder compositeBuilder;


    @Override
    @Transactional
    public void generarFactura(Object eventPayload) {

        // Conversión del evento recibido a su tipo real
        PedidoCompletadoEventDTO evento = (PedidoCompletadoEventDTO) eventPayload;
        log.info("Estrategia PRODUCTOS: Procesando Pedido ID: {}", evento.getPedidoId());


        // Verifica si ya existe una factura para este pedido (idOrigen + tipo)
        if (facturaRepository.existsByIdOrigenAndTipoFactura(evento.getPedidoId(), getTipo())) {
            log.warn("Factura ya existe para Pedido ID: {}. Omitiendo.", evento.getPedidoId());
            return;
        }

        // Creación de una nueva factura del tipo PRODUCTOS
        FacturaProductos factura = new FacturaProductos();
        factura.setIdOrigen(evento.getPedidoId());
        factura.setUsuarioId(evento.getUsuarioId());

        // Generación del número único de factura
        factura.setNumFactura("F-PROD-" + evento.getPedidoId() + "-" + System.currentTimeMillis());

        // Datos base de la factura
        factura.setFechaEmision(evento.getFechaCreacion().toLocalDate());
        factura.setEstadoFactura(EstadoFactura.PAGADA);
        factura.setMetodoPago(MetodoPago.TARJETA_CREDITO);

        // Totales (simples en esta estrategia)
        factura.setTotal(evento.getTotalPedido());
        factura.setSubTotal(evento.getTotalPedido());
        factura.setImpuestos(BigDecimal.ZERO);

        // Conversión de items del pedido en líneas de factura
        Set<LineaFactura> lineas = evento.getItems().stream()
                .map(itemDTO -> {
                    LineaFactura linea = new LineaFactura();
                    linea.setProductoId(itemDTO.getProductoId());
                    linea.setCantidad(itemDTO.getCantidad());
                    linea.setPrecioUnitarioVenta(itemDTO.getPrecioUnitario());

                    // Cálculo de subtotal de la línea
                    linea.setSubtotalLinea(itemDTO.getPrecioUnitario().multiply(new BigDecimal(itemDTO.getCantidad())));

                    // Relación bidireccional
                    linea.setFactura(factura);
                    return linea;
                }).collect(Collectors.toSet());
        factura.setLineas(lineas);


        // Guardado en base de datos
        facturaRepository.save(factura);
        log.info("Factura {} guardada exitosamente en BD.", factura.getNumFactura());

        // Construcción de estructura compuesta (Composite Pattern)
        List<ItemFactura> estructuraFactura = compositeBuilder.construirEstructura(evento.getItems());


        // Construcción del resumen textual del detalle de factura
        StringBuilder detalleCorreo = new StringBuilder();
        for (ItemFactura item : estructuraFactura) {
            detalleCorreo.append(item.generarResumen(0));
        }
        // Envío de notificación por Kafka
        enviarNotificacionFactura(evento, factura, detalleCorreo.toString());
    }


    // Construcción del payload de notificación y envío por Kafka
    private void enviarNotificacionFactura(PedidoCompletadoEventDTO evento, FacturaProductos factura, String detalleItems) {
        Map<String, String> payload = new HashMap<>();
        payload.put("clienteNombre", evento.getClienteNombre());
        payload.put("clienteEmail", evento.getClienteEmail());
        payload.put("numFactura", factura.getNumFactura());
        payload.put("total", factura.getTotal().toString());
        payload.put("fecha", factura.getFechaEmision().format(DateTimeFormatter.ISO_DATE));
        payload.put("detalleItems", detalleItems);

        // Construcción del DTO de notificación
        NotificationRequestDTO notificacion = new NotificationRequestDTO(
                NotificationType.FACTURA,
                payload
        );

        // Envío al microservicio correspondiente mediante Kafka
        notificationProducer.enviarNotificacion(notificacion);
        log.info("Evento de notificación enviado para factura {}", factura.getNumFactura());
    }

    @Override
    // Esta estrategia corresponde únicamente al tipo PRODUCTOS
    public TipoFactura getTipo() {
        return TipoFactura.PRODUCTOS;
    }
}
