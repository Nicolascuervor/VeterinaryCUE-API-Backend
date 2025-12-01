package co.cue.facturas_service.repository;

import co.cue.facturas_service.models.entities.LineaFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Indica que esta interfaz es un componente de acceso a datos administrado por Spring.
// Spring la detecta como un bean de repositorio.
@Repository

// Repositorio JPA para la entidad LineaFactura.
// Hereda métodos CRUD completos (save, findById, delete, findAll, etc.)
// No necesita declarar métodos adicionales porque se usan los básicos.
public interface LineaFacturaRepository extends JpaRepository<LineaFactura, Long> {
// No se definen métodos personalizados por ahora.
    // - Guardar líneas
    // - Buscar líneas por ID
    // - Listar todas las líneas
    // - Eliminar líneas
}
