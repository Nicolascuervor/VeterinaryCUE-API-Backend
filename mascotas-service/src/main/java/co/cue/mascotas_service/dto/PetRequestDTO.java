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
public class PetRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50)
    private String species;

    @Size(max = 50)
    private String breed;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate birthDate;

    @Size(max = 10)
    private String gender;

    @Size(max = 20)
    private String color;

    @Positive(message = "El peso debe ser positivo")
    private Double weight;

    @NotNull(message = "El ID del dueño es obligatorio")
    private Long ownerId;

    @Size(max = 50)
    private String microchipNumber;

    @Size(max = 500)
    private String medicalNotes;
}