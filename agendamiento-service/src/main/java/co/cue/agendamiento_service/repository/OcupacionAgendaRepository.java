package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.OcupacionAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OcupacionAgendaRepository extends JpaRepository<OcupacionAgenda, Long> {

    /**
     * Busca cualquier conflicto de horario.
     * Se usa al intentar crear una nueva cita para ver si ya existe algo en ese hueco.
     * Lógica: "Traeme cualquier ocupación que empiece antes de que yo termine Y termine después de que yo empiece".
     */
    @Query("SELECT o FROM OcupacionAgenda o " +
            "WHERE o.veterinarioId = :veterinarioId " +
            "AND o.fechaInicio < :finNuevo " +
            "AND o.fechaFin > :inicioNuevo")
    List<OcupacionAgenda> findConflictos(
            @Param("veterinarioId") Long veterinarioId,
            @Param("inicioNuevo") LocalDateTime inicioNuevo,
            @Param("finNuevo") LocalDateTime finNuevo
    );

    /**
     * Busca todas las ocupaciones en un rango amplio (ej: vista mensual o semanal del calendario).
     */
    List<OcupacionAgenda> findByVeterinarioIdAndFechaInicioBetween(
            Long veterinarioId,
            LocalDateTime inicioRango,
            LocalDateTime finRango
    );


    void deleteByReferenciaExternaIdAndTipo(Long referenciaExternaId, co.cue.agendamiento_service.models.entities.enums.TipoOcupacion tipo);
}