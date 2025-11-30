package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,  // Define que la deserialización usará el nombre de la subclase
        include = JsonTypeInfo.As.PROPERTY,  // Incluye la información del tipo como una propiedad JSON
        property = "tipoServicio"           // Nombre de la propiedad JSON que indica el tipo de servicio
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsultaRequestDTO.class, name = "CONSULTA"),   // Subtipo Consulta
        @JsonSubTypes.Type(value = CirugiaRequestDTO.class, name = "CIRUGIA"),     // Subtipo Cirugía
        @JsonSubTypes.Type(value = EsteticaRequestDTO.class, name = "ESTETICA"),   // Subtipo Estético
        @JsonSubTypes.Type(value = VacunacionRequestDTO.class, name = "VACUNACION")   // Subtipo Vacunación
})
@Getter  // Genera automáticamente los métodos get para todos los campos de la clase
@Setter  // Genera automáticamente los métodos set para todos los campos de la clase
public abstract class ServicioRequestDTO {         // Clase base abstracta para los DTOs de solicitud de servicios
    private Long id;                               // Identificador del servicio
    private String nombre;                         // Nombre del servicio
    private String descripcion;                    // Descripción detallada del servicio
    private Integer duracionPromedioMinutos;       // Duración promedio del servicio en minutos
    private BigDecimal precio;                     // Precio del servicio
    private boolean activo;                        // Indica si el servicio está activo o disponible
}
