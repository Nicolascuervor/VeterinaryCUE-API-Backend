package co.cue.facturas_service.strategies.concretestrategie;

import co.cue.facturas_service.models.dtos.kafka.PedidoCompletadoEventDTO;
import co.cue.facturas_service.models.entities.FacturaProductos;
import co.cue.facturas_service.models.entities.LineaFactura;
import co.cue.facturas_service.models.enums.EstadoFactura;
import co.cue.facturas_service.models.enums.MetodoPago;
import co.cue.facturas_service.models.enums.TipoFactura;
import co.cue.facturas_service.repository.FacturaRepository;
import co.cue.facturas_service.strategies.IFacturaGenerationStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class ProductoFacturaStrategy implements IFacturaGenerationStrategy {

    private final FacturaRepository facturaRepository;


    @Override
    @Transactional
    public void generarFactura(Object eventPayload) {

        PedidoCompletadoEventDTO evento = (PedidoCompletadoEventDTO) eventPayload;

        log.info("Estrategia PRODUCTOS: Generando factura para Pedido ID: {}", evento.getPedidoId());


        if (facturaRepository.existsByIdOrigenAndTipoFactura(evento.getPedidoId(), getTipo())) {
            log.warn("Ya existe una factura para el Pedido ID: {}. Ignorando evento.", evento.getPedidoId());
            return;
        }

        // --- 1. Mapear Líneas ---
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

        // --- 2. Crear Factura (Hija) ---
        FacturaProductos factura = new FacturaProductos();
        factura.setIdOrigen(evento.getPedidoId()); // Guardamos el ID del Pedido
        factura.setUsuarioId(evento.getUsuarioId());

        // Generar un número de factura (ej. "F-PROD-1001")
        factura.setNumFactura("F-PROD-" + evento.getPedidoId()); // (Lógica de ejemplo)

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
    }

    @Override
    public TipoFactura getTipo() {
        return TipoFactura.PRODUCTOS;
    }
}
