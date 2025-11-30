package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.servicios.VeterinarioServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Marca esta interfaz como un repositorio de Spring, habilitando inyección de dependencias y operaciones de persistencia
public interface VeterinarioServicioRepository extends JpaRepository<VeterinarioServicio, Long> {
    // Extiende JpaRepository para proveer operaciones CRUD para la entidad VeterinarioServicio
    // La entidad principal es VeterinarioServicio y la clave primaria es Long

    /**
     * Busca todas las relaciones entre veterinarios y servicios dado un ID de servicio.
     *
     * @param servicioId El ID del servicio
     * @return Lista de objetos VeterinarioServicio asociados a ese servicio
     */
    List<VeterinarioServicio> findByServicioId(Long servicioId);
}
