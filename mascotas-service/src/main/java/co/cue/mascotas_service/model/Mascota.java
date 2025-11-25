package co.cue.mascotas_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mascota")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad que representa la tabla 'mascota' en la base de datos.
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identificador único generado automáticamente.
    private Long id;

    @Column(nullable = false, length = 100)
    // Nombre de la mascota.
    private String nombre;

    @Column(nullable = false, length = 50)
    // Especie a la que pertenece la mascota.
    private String especie;

    @Column(length = 50)
    // Raza de la mascota (opcional).
    private String raza;

    @Column(nullable = false)
    // Fecha de nacimiento de la mascota.
    private LocalDate fechaNacimiento;

    @Column(length = 10)
    // Sexo de la mascota (Macho / Hembra).
    private String sexo;

    @Column(length = 20)
    // Color principal de la mascota.
    private String color;

    // Peso de la mascota.
    private Double peso;

    @Column(name = "duenioId", nullable = false)
    // ID del dueño asociado a la mascota.
    private Long duenoId;

    @Column(nullable = false)
    @Builder.Default
    // Indica si la mascota está activa en el sistema.
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    // Fecha en que la mascota fue registrada.
    private LocalDateTime createdAt;

    // Fecha de la última actualización del registro.
    private LocalDateTime updatedAt;

    @PrePersist
    // Se ejecuta automáticamente antes de insertar un registro nuevo.
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    // Se ejecuta automáticamente antes de actualizar un registro existente.
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}