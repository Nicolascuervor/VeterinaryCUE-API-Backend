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

    private Long citaId;
    private Long petId;
    private Long veterinarianId;
    private LocalDate fecha;

    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private BigDecimal peso;
    private BigDecimal temperatura;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private String estadoGeneral;
    private String examenesRealizados;
    private String medicamentosRecetados;
    private LocalDate proximaCita;
}