package co.cue.mascotas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseDTO {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private Integer ageInYears;
    private String gender;
    private String color;
    private Double weight;
    private Long ownerId;
    private String microchipNumber;
    private String medicalNotes;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}