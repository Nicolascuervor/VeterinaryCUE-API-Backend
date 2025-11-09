package co.cue.citas_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;



    @Entity
    @Table(name = "Cita")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Cita {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "mascota_id", nullable = false)
        private Long petId;

        @Column(name = "veterinario_id", nullable = false)
        private Long veterinarianId;

        @Column(nullable = false)
        private LocalDate fechayhora;

        @Column(nullable = false, length = 1000)
        private String motivo;

        @Column(length = 2000)
        private String observaciones;


        @Column(name = "estado_general", length = 500)
        private String estadoGeneral;

        @Column(nullable = false)
        private EstadoCita estado;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

}
