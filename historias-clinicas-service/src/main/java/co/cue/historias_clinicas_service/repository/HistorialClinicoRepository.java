package co.cue.historias_clinicas_service.repository;

import co.cue.historias_clinicas_service.entity.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico,Long> {
    // Busca todos los historiales clínicos asociados a un ID de mascota
    List<HistorialClinico> findByPetId(Long petId);
    // Verifica si ya existe un historial clínico para una cita específica
    boolean existsByCitaId(Long citaId);
}
