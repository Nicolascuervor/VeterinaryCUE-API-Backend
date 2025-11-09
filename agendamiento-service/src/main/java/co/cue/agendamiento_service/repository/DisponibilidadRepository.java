package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.Disponibilidad;
import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {


    List<Disponibilidad> findByVeterinarioIdAndFechaHoraInicioBetweenAndEstado(
            Long veterinarioId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoDisponibilidad estado
    );

    List<Disponibilidad> findByCitaId(Long citaId);


    @Modifying
    @Query("UPDATE Disponibilidad d SET d.estado = :nuevoEstado, d.citaId = :citaId " +
            "WHERE d.id IN :ids AND d.estado = 'DISPONIBLE'")
    int reservarSlotsMasivo(
            @Param("ids") List<Long> ids,
            @Param("citaId") Long citaId,
            @Param("nuevoEstado") EstadoDisponibilidad nuevoEstado
    );
}