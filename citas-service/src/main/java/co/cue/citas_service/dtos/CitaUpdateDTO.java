package co.cue.citas_service.dtos;

import co.cue.citas_service.entity.EstadoCita;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class CitaUpdateDTO {

    // Observaciones de la cita
    private String observaciones;

    // Estado general de la mascota
    private String estadoGeneralMascota;

    // Estado de la cita
    private EstadoCita estado;

    // Diagnóstico de la mascota
    private String diagnostico;

    // Tratamiento indicado
    private String tratamiento;

    // Peso de la mascota
    private BigDecimal peso;

    // Temperatura de la mascota
    private BigDecimal temperatura;

    // Frecuencia cardiaca de la mascota
    private Integer frecuenciaCardiaca;

    // Frecuencia respiratoria de la mascota
    private Integer frecuenciaRespiratoria;

    // Exámenes realizados
    private String examenesRealizados;

    // Medicamentos recetados
    private String medicamentosRecetados;

    // Fecha de la próxima cita
    private LocalDate proximaCita;

    // Fecha y hora de inicio de la cita (para reasignación de horario)
    private LocalDateTime fechaHoraInicio;

    // Fecha y hora de fin de la cita (opcional, se calcula si no se proporciona)
    private LocalDateTime fechaHoraFin;
}