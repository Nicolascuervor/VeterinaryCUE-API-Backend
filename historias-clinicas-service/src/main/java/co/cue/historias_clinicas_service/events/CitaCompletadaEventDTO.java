package co.cue.historias_clinicas_service.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaCompletadaEventDTO {
    // ID único de la cita que fue completada
    private Long citaId;

    // ID de la mascota asociada a la cita
    private Long petId;

    // ID del veterinario que atendió la cita
    private Long veterinarianId;

    // Fecha en que se realizó la cita
    private LocalDate fecha;

    // Diagnóstico realizado por el veterinario
    private String diagnostico;

    // Tratamiento recomendado o aplicado
    private String tratamiento;

    // Observaciones adicionales del veterinario
    private String observaciones;

    // Peso de la mascota registrado en la cita
    private BigDecimal peso;

    // Temperatura de la mascota registrada
    private BigDecimal temperatura;

    // Frecuencia cardíaca medida durante la cita
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria medida durante la cita
    private Integer frecuenciaRespiratoria;

    // Estado general de la mascota (ej. "Estable", "Crítico")
    private String estadoGeneral;

    // Exámenes realizados durante la cita
    private String examenesRealizados;

    // Medicamentos recetados
    private String medicamentosRecetados;

    // Fecha sugerida para la próxima cita
    private LocalDate proximaCita;
}
