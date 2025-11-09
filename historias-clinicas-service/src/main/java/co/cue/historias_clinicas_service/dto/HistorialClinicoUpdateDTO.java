package co.cue.historias_clinicas_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinicoUpdateDTO {

    @Size(max = 1000, message = "El diagnóstico no puede exceder los 1000 caracteres")
    private String diagnostico;

    @Size(max = 2000, message = "El tratamiento no puede exceder los 2000 caracteres")
    private String tratamiento;

    @Size(max = 2000, message = "Las observaciones no pueden exceder los 2000 caracteres")
    private String observaciones;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "999.99", message = "El peso no puede exceder 999.99 kg")
    private BigDecimal peso;

    @DecimalMin(value = "30.0", message = "La temperatura debe ser al menos 30°C")
    @DecimalMax(value = "45.0", message = "La temperatura no puede exceder 45°C")
    private BigDecimal temperatura;

    @Min(value = 40, message = "La frecuencia cardíaca debe ser al menos 40 lpm")
    @Max(value = 300, message = "La frecuencia cardíaca no puede exceder 300 lpm")
    private Integer frecuenciaCardiaca;

    @Min(value = 10, message = "La frecuencia respiratoria debe ser al menos 10 rpm")
    @Max(value = 100, message = "La frecuencia respiratoria no puede exceder 100 rpm")
    private Integer frecuenciaRespiratoria;

    @Size(max = 500, message = "El estado general no puede exceder los 500 caracteres")
    private String estadoGeneral;

    @Size(max = 1000, message = "Los exámenes realizados no pueden exceder los 1000 caracteres")
    private String examenesRealizados;

    @Size(max = 1000, message = "Los medicamentos recetados no pueden exceder los 1000 caracteres")
    private String medicamentosRecetados;

    @Future(message = "La próxima cita debe ser una fecha futura")
    private LocalDate proximaCita;
}