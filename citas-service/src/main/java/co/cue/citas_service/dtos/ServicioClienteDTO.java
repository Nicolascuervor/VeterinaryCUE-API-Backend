package co.cue.citas_service.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicioClienteDTO {

    // Id del servicio
    private Long id;

    // Nombre del servicio
    private String nombre;

    // Duración promedio en minutos del servicio
    private Integer duracionPromedioMinutos;

    // Precio del servicio
    private BigDecimal precio;

    // Tipo de servicio
    private String tipoServicio;
}
