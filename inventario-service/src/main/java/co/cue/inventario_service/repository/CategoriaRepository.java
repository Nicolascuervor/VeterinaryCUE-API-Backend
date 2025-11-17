package co.cue.inventario_service.repository;

import co.cue.inventario_service.models.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByIdAndActivoTrue(Long id);
    List<Categoria> findAllByActivoTrue();
    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
