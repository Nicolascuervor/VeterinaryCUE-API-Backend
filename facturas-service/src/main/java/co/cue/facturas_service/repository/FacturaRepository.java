package co.cue.facturas_service.repository;

import co.cue.facturas_service.models.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    boolean existsByIdOrigenAndTipoFactura(Long idOrigen, co.cue.facturas_service.models.enums.TipoFactura tipo);
    List<Factura> findByUsuarioId(Long usuarioId);
}
