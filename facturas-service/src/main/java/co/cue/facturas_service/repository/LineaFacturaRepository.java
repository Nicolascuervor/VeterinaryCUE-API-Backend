package co.cue.facturas_service.repository;

import co.cue.facturas_service.models.entities.LineaFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaFacturaRepository extends JpaRepository<LineaFactura, Long> {

}
