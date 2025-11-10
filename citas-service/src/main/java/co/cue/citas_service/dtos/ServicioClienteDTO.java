package co.cue.citas_service.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicioClienteDTO {
    private Long id;
    private String nombre;
    private Integer duracionPromedioMinutos;
    private BigDecimal precio;
    private String tipoServicio;
}
