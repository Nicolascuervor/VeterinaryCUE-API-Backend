package co.cue.mascotas_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO que recibe los datos necesarios para crear o actualizar una mascota.
public class MascotaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    // Nombre de la mascota.
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50)
    // Especie de la mascota (perro, gato, ave, etc.).
    private String especie;

    @Size(max = 50)
    // Raza específica de la mascota (opcional).
    private String raza;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    // Fecha de nacimiento de la mascota.
    private LocalDate fechaNacimiento;

    @Size(max = 10)
    // Sexo de la mascota (Macho / Hembra).
    private String sexo;

    @Size(max = 20)
    // Color predominante de la mascota.
    private String color;

    @Positive(message = "El peso debe ser positivo")
    // Peso aproximado de la mascota.
    private Double peso;

    // Identificador del dueño asociado a la mascota.
    private Long duenioId;
}