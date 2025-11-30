package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.servicios.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Marca esta interfaz como un repositorio de Spring, habilitando inyección de dependencias y operaciones de persistencia
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    // Extiende JpaRepository para proveer operaciones CRUD para la entidad Servicio
    // La entidad principal es Servicio y la clave primaria es Long

    /**
     * Obtiene todos los servicios que estén activos.
     *
     * @return Lista de servicios donde el campo 'activo' es true
     */


    List<Servicio> findAllByActivoTrue();
}