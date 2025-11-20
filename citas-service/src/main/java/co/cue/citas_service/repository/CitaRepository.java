package co.cue.citas_service.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    Optional<Cita> findCitaById(Long id);
    List<Cita> findAllByEstado(EstadoCita estado);
    List<Cita> findAllByFechaHoraInicioBetween(LocalDateTime inicioDelDia, LocalDateTime finDelDia);
}
