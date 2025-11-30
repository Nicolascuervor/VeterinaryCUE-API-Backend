package co.cue.inventario_service.repository;

import co.cue.inventario_service.models.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Busca un producto por ID que además esté activo (activo = true)
    Optional<Producto> findByIdAndActivoTrue(Long id);

    // Obtiene todos los productos activos
    List<Producto> findAllByActivoTrue();

    // Busca un producto por su nombre siempre y cuando esté activo
    Optional<Producto> findByNombreAndActivoTrue(String nombre);

    // Verifica si existe un producto con un nombre específico y que esté activo
    boolean existsByNombreAndActivoTrue(String nombre);
}
