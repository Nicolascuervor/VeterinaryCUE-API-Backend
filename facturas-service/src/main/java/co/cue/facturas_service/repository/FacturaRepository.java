package co.cue.facturas_service.repository;

import co.cue.facturas_service.models.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
// Indica que esta interfaz es un componente de acceso a datos administrado por Spring.
@Repository

// Repositorio que gestiona entidades de tipo Factura.
// Extiende JpaRepository para obtener CRUD completo sin implementar nada.
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    // Verifica si existe una factura según:
    // - idOrigen: ID de la entidad que originó la factura (ej: pedido, cita)
    // - tipoFactura: enum que indica si es FACTURA DE PRODUCTOS o FACTURA DE CITA
    //
    // Se usa para evitar generar facturas duplicadas.
    boolean existsByIdOrigenAndTipoFactura(Long idOrigen, co.cue.facturas_service.models.enums.TipoFactura tipo);

    // Retorna la lista de facturas asociadas a un usuario específico.
    // Permite consultar el historial de facturación del usuario.
    List<Factura> findByUsuarioId(Long usuarioId);
}
