package co.cue.historias_clinicas_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinicoUpdateDTO {

    // Diagnóstico actualizado para el historial clínico
    private String diagnostico;

    // Tratamiento actualizado indicado por el veterinario
    private String tratamiento;

    // Observaciones adicionales o notas relevantes
    private String observaciones;

    // Peso actualizado de la mascota
    private BigDecimal peso;

    // Temperatura actual de la mascota
    private BigDecimal temperatura;

    // Frecuencia cardíaca actual
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria actual
    private Integer frecuenciaRespiratoria;

    // Estado general de la mascota (ej: estable, crítico, etc.)
    private String estadoGeneral;

    // Exámenes realizados durante la consulta
    private String examenesRealizados;

    // Medicamentos recetados en la actualización
    private String medicamentosRecetados;

    // Fecha programada para la próxima cita
    private LocalDate proximaCita;
}