package co.cue.citas_service.events;

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

    // Id de la cita
    private Long citaId;

    // Id de la mascota
    private Long petId;

    // Id del veterinario
    private Long veterinarianId;

    // Fecha de la cita
    private LocalDate fecha;

    // Diagnóstico de la mascota
    private String diagnostico;

    // Tratamiento realizado
    private String tratamiento;

    // Observaciones de la cita
    private String observaciones;

    // Peso de la mascota
    private BigDecimal peso;

    // Temperatura de la mascota
    private BigDecimal temperatura;

    // Frecuencia cardiaca de la mascota
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria de la mascota
    private Integer frecuenciaRespiratoria;

    // Estado general de la mascota
    private String estadoGeneral;

    // Exámenes realizados
    private String examenesRealizados;

    // Medicamentos recetados
    private String medicamentosRecetados;

    // Fecha de la próxima cita
    private LocalDate proximaCita;
}