package co.cue.inventario_service.repository;

import co.cue.inventario_service.models.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Busca una categoría por ID siempre y cuando esté activa (activo = true)
    Optional<Categoria> findByIdAndActivoTrue(Long id);


    // Lista todas las categorías que están activas en la base de datos
    List<Categoria> findAllByActivoTrue();

    // Busca una categoría por su nombre
    Optional<Categoria> findByNombre(String nombre);

    // Verifica si existe una categoría registrada con un nombre específico
    boolean existsByNombre(String nombre);
}
