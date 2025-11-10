package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.servicios.VeterinarioServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeterinarioServicioRepository extends JpaRepository<VeterinarioServicio, Long> {
    List<VeterinarioServicio> findByServicioId(Long servicioId);
}
