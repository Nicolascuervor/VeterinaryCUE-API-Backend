package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.JornadaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface JornadaLaboralRepository extends JpaRepository<JornadaLaboral, Long> {

    Optional<JornadaLaboral> findByVeterinarioIdAndDiaSemana(Long veterinarioId, DayOfWeek diaSemana);
    List<JornadaLaboral> findByVeterinarioIdAndActivaTrue(Long veterinarioId);
}