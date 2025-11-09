package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos;

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
        @JsonSubTypes.Type(value = ConsultaRequestDTO.class, name = "CONSULTA"),
        @JsonSubTypes.Type(value = CirugiaRequestDTO.class, name = "CIRUGIA"),
        @JsonSubTypes.Type(value = EsteticaRequestDTO.class, name = "ESTETICA"),
        @JsonSubTypes.Type(value = VacunacionRequestDTO.class, name = "VACUNACION")
})
@Getter
@Setter
public abstract class ServicioRequestDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionPromedioMinutos;
    private BigDecimal precio;
    private boolean activo;
}
