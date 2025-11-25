package co.cue.mascotas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO usado para enviar la información completa de una mascota en las respuestas.
public class MascotaResponseDTO {

    // Identificador único de la mascota.
    private Long id;

    // Nombre de la mascota.
    private String nombre;

    // Especie a la que pertenece la mascota.
    private String especie;

    // Raza de la mascota (puede ser opcional).
    private String raza;

    // Fecha de nacimiento de la mascota.
    private LocalDate fechaNacimiento;

    // Dato adicional (si el sistema lo requiere).
    private Integer dato;

    // Sexo de la mascota.
    private String sexo;

    // Color principal de la mascota.
    private String color;

    // Peso de la mascota.
    private Double peso;

    // Identificador del dueño asociado.
    private Long duenioId;

    // Indica si la mascota está activa dentro del sistema.
    private Boolean active;
}