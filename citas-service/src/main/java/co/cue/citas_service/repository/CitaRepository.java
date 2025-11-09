package co.cue.citas_service.repository;
import java.util.Optional;
import co.cue.citas_service.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    Optional<Cita> findCitaById(Long id);
}
