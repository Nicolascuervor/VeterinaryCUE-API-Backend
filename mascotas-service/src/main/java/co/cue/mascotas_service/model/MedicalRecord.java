package co.cue.mascotas_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long petId;

    @Column(nullable = false)
    private LocalDateTime recordDate;

    @Column(nullable = false, length = 100)
    private String recordType;

    @Column(length = 1000)
    private String description;

    @Column(length = 200)
    private String diagnosis;

    @Column(length = 500)
    private String treatment;

    private Long veterinarianId;

    @Column(length = 200)
    private String veterinarianName;

    private Double cost;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}