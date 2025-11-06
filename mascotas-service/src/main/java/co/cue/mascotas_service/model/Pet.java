package co.cue.mascotas_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String species;

    @Column(length = 50)
    private String breed;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 10)
    private String gender;

    @Column(length = 20)
    private String color;

    private Double weight;

    @Column(nullable = false)
    private Long ownerId;

    @Column(length = 50)
    private String microchipNumber;

    @Column(length = 500)
    private String medicalNotes;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}