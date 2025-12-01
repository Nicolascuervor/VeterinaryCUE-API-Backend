package co.cue.citas_service.dtos;


import co.cue.citas_service.entity.EstadoCita;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CitaDetailDTO {
    private Long id;

    // Información Básica (La misma de la tabla)
    private Long duenioId;
    private Long petId;
    private Long veterinarianId;
    private Long servicioId;
    private LocalDateTime fechaHoraInicio; // ¡Aquí va la hora!
    private EstadoCita estado;
    private String nombreServicio;
    private BigDecimal precioServicio;

    // INFORMACIÓN CLÍNICA COMPLETA
    private String motivoConsulta;
    private String estadoGeneralMascota;
    private Double peso;
    private Double temperatura;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private String diagnostico;
    private String tratamiento;
    private String medicamentosRecetados;
    private String observaciones;
    private String examenesRealizados;
}
