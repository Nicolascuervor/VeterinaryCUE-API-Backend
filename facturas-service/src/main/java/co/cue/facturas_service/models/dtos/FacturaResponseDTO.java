package co.cue.facturas_service.models.dtos;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
// Genera automáticamente los métodos getter para todos los atributos
// Genera automáticamente los métodos setter para todos los atributos
@Getter @Setter
public class FacturaResponseDTO {
    private Long id;                     // Identificador único de la factura.
    private String numFactura;           // Número de factura generado (puede contener prefijo, serie, etc.).
    private LocalDate fechaEmision;       // Fecha en la que la factura fue emitida.
    private BigDecimal total;             // Total final de la factura, sumando todos los ítems e impuestos.
    private String estado;                // Estado de la factura (por ejemplo: PAGADA, PENDIENTE, CANCELADA).
    private String metodoPago;             // Método de pago utilizado (tarjeta, efectivo, transferencia, etc.).
    private List<LineaFacturaDTO> lineas;     // Lista de líneas o detalles incluidos en la factura.

    @Getter
    @Setter
    // CLASE INTERNA: Representa cada línea/detalle de la factura
    public static class LineaFacturaDTO {
        private String productoNombre;         // Nombre del producto o servicio facturado.
        private Long productoId;                // Identificador del producto asociado a esta línea.
        private Integer cantidad;               // Cantidad del producto o servicio facturado.
        private BigDecimal precioUnitario;      // Precio unitario del producto.
        private BigDecimal subtotal;            // Subtotal calculado: precioUnitario * cantidad.
    }
}
