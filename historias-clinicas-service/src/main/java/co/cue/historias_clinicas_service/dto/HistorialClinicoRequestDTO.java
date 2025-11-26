package co.cue.historias_clinicas_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * DTO utilizado para recibir la información necesaria
 * para crear o actualizar un historial clínico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinicoRequestDTO {
    // ID de la mascota asociada al historial clínico
    private Long petId;

    // ID del veterinario que registra o actualiza el historial
    private Long veterinarianId;

    // Fecha en la que se realizó la revisión o consulta médica
    private LocalDate fecha;

    // Diagnóstico realizado por el veterinario
    private String diagnostico;

    // Tratamiento recomendado o aplicado a la mascota
    private String tratamiento;

    // Observaciones adicionales relevantes del estado del paciente
    private String observaciones;

    // Peso registrado de la mascota en la consulta
    private BigDecimal peso;

    // Temperatura corporal de la mascota
    private BigDecimal temperatura;

    // Frecuencia cardíaca de la mascota por minuto
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria de la mascota por minuto
    private Integer frecuenciaRespiratoria;

    // Estado general de la mascota según evaluación (bueno, estable, crítico, etc.)
    private String estadoGeneral;

    // Exámenes realizados durante la consulta (laboratorio, imagenología, etc.)
    private String examenesRealizados;

    // Medicamentos recetados al paciente
    private String medicamentosRecetados;

    // Fecha sugerida para la próxima consulta
    private LocalDate proximaCita;

}