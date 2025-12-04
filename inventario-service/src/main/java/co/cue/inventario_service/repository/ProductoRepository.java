package co.cue.inventario_service.repository;

import co.cue.inventario_service.models.entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Obtiene todos los productos activos y disponibles para venta (para ecommerce)
    List<Producto> findAllByActivoTrueAndDisponibleParaVentaTrue();

    // Obtiene productos activos y disponibles para venta con paginación
    Page<Producto> findAllByActivoTrueAndDisponibleParaVentaTrue(Pageable pageable);

    // Busca productos por nombre (búsqueda parcial, case-insensitive) y disponibles para venta
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.disponibleParaVenta = true " +
           "AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Producto> buscarProductosPorNombre(@Param("nombre") String nombre, Pageable pageable);

    // Busca productos por categoría y disponibles para venta
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.disponibleParaVenta = true " +
           "AND p.categoria.id = :categoriaId")
    Page<Producto> buscarProductosPorCategoria(@Param("categoriaId") Long categoriaId, Pageable pageable);

    // Búsqueda combinada: nombre, categoría y rango de precios
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.disponibleParaVenta = true " +
           "AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
           "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    Page<Producto> buscarProductosConFiltros(
            @Param("nombre") String nombre,
            @Param("categoriaId") Long categoriaId,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax,
            Pageable pageable);
}
