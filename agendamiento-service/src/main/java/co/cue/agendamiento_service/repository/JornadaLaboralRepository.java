package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.JornadaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository // Marca la interfaz como repositorio de Spring, habilitando inyección y manejo de persistencia
public interface JornadaLaboralRepository extends JpaRepository<JornadaLaboral, Long> {
// Extiende JpaRepository para proveer operaciones CRUD para la entidad JornadaLaboral
    // La entidad principal es JornadaLaboral y la clave primaria es Long

    /**
     * Busca la jornada laboral de un veterinario específico para un día de la semana.
     *
     * @param veterinarioId ID del veterinario
     * @param diaSemana     Día de la semana a consultar
     * @return Optional con la JornadaLaboral si existe, vacío si no se encuentra
     */
    Optional<JornadaLaboral> findByVeterinarioIdAndDiaSemana(Long veterinarioId, DayOfWeek diaSemana);

    /**
     * Obtiene todas las jornadas activas de un veterinario.
     *
     * @param veterinarioId ID del veterinario
     * @return Lista de jornadas activas del veterinario
     */
    List<JornadaLaboral> findByVeterinarioIdAndActivaTrue(Long veterinarioId);

    List<JornadaLaboral> findAllByVeterinarioId(Long veterinarioId);
}