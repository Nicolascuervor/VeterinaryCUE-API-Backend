package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,   // Indica que la deserialización se hará usando el nombre del tipo
        include = JsonTypeInfo.As.PROPERTY,    // Se incluirá como propiedad en el JSON
        property = "tipoServicio"              // El nombre de la propiedad JSON que identifica el tipo concreto de servicio
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsultaResponseDTO.class, name = "CONSULTA"),   // Mapea tipo "CONSULTA" a ConsultaResponseDTO
        @JsonSubTypes.Type(value = CirugiaResponseDTO.class, name = "CIRUGIA"),     // Mapea tipo "CIRUGIA" a CirugiaResponseDTO
        @JsonSubTypes.Type(value = EsteticaResponseDTO.class, name = "ESTETICA"),   // Mapea tipo "ESTETICA" a EsteticaResponseDTO
        @JsonSubTypes.Type(value = VacunacionResponseDTO.class, name = "VACUNACION")  // Mapea tipo "VACUNACION" a VacunacionResponseDTO
})
@Getter  // Genera automáticamente los métodos get para todos los campos
@Setter  // Genera automáticamente los métodos set para todos los campos
public abstract class ServicioResponseDTO {     // DTO base abstracto para todos los tipos de servicios en la respuesta
    private Long id;                            // ID único del servicio
    private String nombre;                      // Nombre del servicio
    private String descripcion;                 // Descripción del servicio
    private Integer duracionPromedioMinutos;    // Duración promedio del servicio en minutos
    private BigDecimal precio;                  // Precio del servicio
    private boolean activo;                     // Indica si el servicio está activo o desactivado
}
