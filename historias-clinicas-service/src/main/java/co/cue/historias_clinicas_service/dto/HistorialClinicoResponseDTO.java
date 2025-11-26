package co.cue.historias_clinicas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinicoResponseDTO {

    // Identificador único del historial clínico
    private Long id;

    // ID de la mascota a la que pertenece el historial
    private Long petId;

    // ID del veterinario que creó o atendió el historial
    private Long veterinarianId;

    // Fecha en la que se realizó la consulta o atención médica
    private LocalDate fecha;

    // Diagnóstico realizado por el veterinario
    private String diagnostico;

    // Tratamiento asignado a la mascota
    private String tratamiento;

    // Observaciones adicionales relevantes en la consulta
    private String observaciones;

    // Peso registrado durante la cita
    private BigDecimal peso;

    // Temperatura corporal registrada
    private BigDecimal temperatura;

    // Frecuencia cardíaca registrada
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria registrada
    private Integer frecuenciaRespiratoria;

    // Estado general de la mascota según la evaluación del veterinario
    private String estadoGeneral;

    // Exámenes realizados durante la consulta
    private String examenesRealizados;

    // Medicamentos recetados en la visita médica
    private String medicamentosRecetados;

    // Fecha programada para la próxima cita
    private LocalDate proximaCita;

    // Indica si el historial está activo o fue dado de baja
    private Boolean activo;

    // Fecha y hora en la que se creó el registro
    private LocalDateTime createdAt;

    // Fecha y hora de la última actualización del registro
    private LocalDateTime updatedAt;
}
