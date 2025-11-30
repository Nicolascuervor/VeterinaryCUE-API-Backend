package co.cue.agendamiento_service.models.entities;

import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity   // Marca la clase como entidad JPA para persistencia en la base de datos
@Table(name = "disponibilidad_veterinarios", indexes = {  // Define la tabla específica de disponibilidad de veterinarios
        @Index(name = "idx_vet_inicio", columnList = "veterinario_id, fecha_hora_inicio")})
// Crea un índice para optimizar consultas por veterinario y fecha de inicio
@Getter    // Genera automáticamente los getters para todos los campos
@Setter    // Genera automáticamente los setters para todos los campos
@NoArgsConstructor      // Genera un constructor sin parámetros
public class Disponibilidad {

    @Id                                                  // Define la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // La clave primaria se genera automáticamente por la base de datos
    private Long id;

    @Column(name = "veterinario_id", nullable = false)
    // Columna obligatoria que almacena el ID del veterinario
    private Long veterinarioId;

    @Column(name = "fecha_hora_inicio", nullable = false)
    // Columna obligatoria que almacena la fecha y hora de inicio del slot
    private LocalDateTime fechaHoraInicio;


    @Column(name = "fecha_hora_fin", nullable = false)
    // Columna obligatoria que almacena la fecha y hora de fin del slot
    private LocalDateTime fechaHoraFin;


    @Enumerated(EnumType.STRING)
    // Indica que el enum se guardará como String en la base de datos
    @Column(nullable = false, length = 20)
    // Columna obligatoria que almacena el estado del slot con longitud máxima 20
    private EstadoDisponibilidad estado;


    @Column(name = "cita_id", nullable = true, unique = false)
    // Columna opcional que almacena la cita asociada al slot
    private Long citaId;


    // Constructor que inicializa un slot con estado DISPONIBLE y sin cita asociada
    public Disponibilidad(Long veterinarioId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        this.veterinarioId = veterinarioId;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = EstadoDisponibilidad.DISPONIBLE;
        this.citaId = null;
    }
}
