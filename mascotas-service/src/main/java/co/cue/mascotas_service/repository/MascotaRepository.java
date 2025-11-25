package co.cue.mascotas_service.repository;

import co.cue.mascotas_service.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    // Buscar todas las mascotas de un dueño específico
    List<Mascota> findByDuenoId(Long duenoId);

    // Obtener mascotas activas (Active = true)
    List<Mascota> findByActiveTrue();

    // Buscar mascotas por nombre, ignorando mayúsculas y minúsculas
    @Query("SELECT p FROM Mascota p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Mascota> searchByName(@Param("nombre") String name);
}