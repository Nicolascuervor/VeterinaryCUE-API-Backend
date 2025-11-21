package co.cue.facturas_service.models.dtos;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class FacturaResponseDTO {
    private Long id;
    private String numFactura;
    private LocalDate fechaEmision;
    private BigDecimal total;
    private String estado;
    private String metodoPago;
    private List<LineaFacturaDTO> lineas;

    @Getter
    @Setter
    public static class LineaFacturaDTO {
        private String productoNombre;
        private Long productoId;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
