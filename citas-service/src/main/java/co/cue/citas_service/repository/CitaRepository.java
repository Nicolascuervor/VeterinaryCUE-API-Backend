package co.cue.citas_service.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio para la entidad Cita
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Buscar una cita por su id
    Optional<Cita> findCitaById(Long id);

    // Buscar todas las citas por estado
    List<Cita> findAllByEstado(EstadoCita estado);

    // Buscar todas las citas cuyo inicio esté entre dos fechas
    List<Cita> findAllByFechaHoraInicioBetween(LocalDateTime inicioDelDia, LocalDateTime finDelDia);
    
    // Buscar una cita por su token de confirmación
    Optional<Cita> findByTokenConfirmacion(String tokenConfirmacion);
    
    // Buscar citas futuras/pendientes de un veterinario
    // Citas con fechaHoraInicio >= fechaActual y estados pendientes (ESPERA, CONFIRMADA, EN_PROGRESO)
    List<Cita> findByVeterinarianIdAndFechaHoraInicioGreaterThanEqualAndEstadoIn(
            Long veterinarianId, 
            LocalDateTime fechaActual, 
            List<EstadoCita> estados);
}
