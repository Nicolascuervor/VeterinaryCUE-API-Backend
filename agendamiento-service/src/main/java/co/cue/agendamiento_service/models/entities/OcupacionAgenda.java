package co.cue.agendamiento_service.models.entities;

import co.cue.agendamiento_service.models.entities.enums.TipoOcupacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Table(name = "ocupaciones_agenda", indexes = {
        // Índice crítico: El 99% de las veces buscaremos "ocupaciones de tal veterinario en tal rango de fechas".
        // Este índice compuesto hace que esa búsqueda sea instantánea.
        @Index(name = "idx_vet_rango", columnList = "veterinario_id, fecha_inicio, fecha_fin")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OcupacionAgenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarioId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    // Referencia opcional al ID de la Cita (si es tipo CITA).
    // No usamos @OneToOne con JPA hacia otro microservicio, solo guardamos el ID numérico.
    @Column(name = "referencia_externa_id")
    private Long referenciaExternaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoOcupacion tipo;

    @Column(length = 255)
    private String observacion; // Ej: "Almuerzo", "Cita con Firulais"
}