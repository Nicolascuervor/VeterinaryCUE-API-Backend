package co.cue.mascotas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDTO {
    private Long id;
    private Long petId;
    private String petName;
    private LocalDateTime recordDate;
    private String recordType;
    private String description;
    private String diagnosis;
    private String treatment;
    private Long veterinarianId;
    private String veterinarianName;
    private Double cost;
    private LocalDateTime createdAt;
}