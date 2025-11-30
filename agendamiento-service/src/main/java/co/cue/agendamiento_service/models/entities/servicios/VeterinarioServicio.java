package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity  // Marca la clase como entidad JPA para persistencia en la base de datos
@Table(name = "veterinario_servicios")    // Define la tabla específica que relaciona veterinarios con servicios
@Getter                                  //Genera automáticamente los getters para todos los campos
@Setter                                   // Genera automáticamente los setters para todos los campos
@NoArgsConstructor
public class VeterinarioServicio {
    @Id       // Define la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // La clave primaria se genera automáticamente por la base de datos
    private Long id;

    @Column(nullable = false) // Columna obligatoria que almacena el ID del veterinario
    private Long veterinarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    // Relación de muchos a uno con la entidad Servicio, carga perezosa
    @JoinColumn(name = "servicio_id", nullable = false)
    // Define la columna de unión y la hace obligatoria
    private Servicio servicio;  // Referencia al servicio asociado al veterinario
}