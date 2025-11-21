package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoServicio"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsultaResponseDTO.class, name = "CONSULTA"),
        @JsonSubTypes.Type(value = CirugiaResponseDTO.class, name = "CIRUGIA"),
        @JsonSubTypes.Type(value = EsteticaResponseDTO.class, name = "ESTETICA"),
        @JsonSubTypes.Type(value = VacunacionResponseDTO.class, name = "VACUNACION")
})
@Getter
@Setter
public abstract class ServicioResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionPromedioMinutos;
    private BigDecimal precio;
    private boolean activo;
}
