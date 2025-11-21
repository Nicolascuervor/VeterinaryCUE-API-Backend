package co.cue.inventario_service.repository;

import co.cue.inventario_service.models.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByIdAndActivoTrue(Long id);

    List<Producto> findAllByActivoTrue();

    Optional<Producto> findByNombreAndActivoTrue(String nombre);
    boolean existsByNombreAndActivoTrue(String nombre);
}
