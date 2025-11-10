package co.cue.citas_service.dtos;

import co.cue.citas_service.entity.EstadoCita;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CitaUpdateDTO {
    private String observaciones;
    private String estadoGeneralMascota;
    private EstadoCita estado;
    private String diagnostico;
    private String tratamiento;
    private BigDecimal peso;
    private BigDecimal temperatura;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private String examenesRealizados;
    private String medicamentosRecetados;
    private LocalDate proximaCita;
}