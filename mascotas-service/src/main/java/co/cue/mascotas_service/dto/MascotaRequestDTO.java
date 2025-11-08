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
public class MascotaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50)
    private String especie;

    @Size(max = 50)
    private String raza;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Size(max = 10)
    private String sexo;

    @Size(max = 20)
    private String color;

    @Positive(message = "El peso debe ser positivo")
    private Double peso;

    private Long duenioId;
}