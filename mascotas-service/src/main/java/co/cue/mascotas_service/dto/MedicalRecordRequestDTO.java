package co.cue.mascotas_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequestDTO {

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long petId;

    @NotNull(message = "La fecha del registro es obligatoria")
    private LocalDateTime recordDate;

    @NotBlank(message = "El tipo de registro es obligatorio")
    @Size(max = 100)
    private String recordType;

    @Size(max = 1000)
    private String description;

    @Size(max = 200)
    private String diagnosis;

    @Size(max = 500)
    private String treatment;

    private Long veterinarianId;

    @Size(max = 200)
    private String veterinarianName;

    @PositiveOrZero(message = "El costo no puede ser negativo")
    private Double cost;
}