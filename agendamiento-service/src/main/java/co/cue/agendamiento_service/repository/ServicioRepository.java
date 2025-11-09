package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.servicios.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findAllByActivoTrue();
}