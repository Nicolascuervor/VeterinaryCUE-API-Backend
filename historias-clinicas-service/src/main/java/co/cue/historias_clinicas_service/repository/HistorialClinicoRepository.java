package co.cue.historias_clinicas_service.repository;

import co.cue.historias_clinicas_service.entity.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico,Long> {
    List<HistorialClinico> findByPetId(Long petId);
    List<HistorialClinico> findByVeterinarioId(Long veterinarioId);
    List<HistorialClinico> findByNombreMascota(Long veterinarioId);
}
