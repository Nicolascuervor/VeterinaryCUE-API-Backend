package co.cue.facturas_service.services;

import co.cue.facturas_service.models.dtos.FacturaResponseDTO;
import co.cue.facturas_service.repository.FacturaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import co.cue.facturas_service.models.entities.Factura;
import co.cue.facturas_service.models.entities.FacturaProductos;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@AllArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;

    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> listarTodas() {
        return facturaRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> listarPorUsuario(Long usuarioId) {
        return facturaRepository.findByUsuarioId(usuarioId).stream().map(this::mapToDTO).toList();
    }

    @Transactional(readOnly = true)
    public FacturaResponseDTO buscarPorId(Long id) {
        return facturaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada"));
    }

    private FacturaResponseDTO mapToDTO(Factura f) {
        FacturaResponseDTO dto = new FacturaResponseDTO();
        dto.setId(f.getId());
        dto.setNumFactura(f.getNumFactura());
        dto.setFechaEmision(f.getFechaEmision());
        dto.setTotal(f.getTotal());
        dto.setEstado(f.getEstadoFactura().name());
        dto.setMetodoPago(f.getMetodoPago().name());

        if (f instanceof FacturaProductos fp) {
            dto.setLineas(fp.getLineas().stream().map(l -> {
                FacturaResponseDTO.LineaFacturaDTO lDto = new FacturaResponseDTO.LineaFacturaDTO();
                lDto.setProductoId(l.getProductoId());
                lDto.setCantidad(l.getCantidad());
                lDto.setPrecioUnitario(l.getPrecioUnitarioVenta());
                lDto.setSubtotal(l.getSubtotalLinea());
                return lDto;
            }).toList());
        }
        return dto;
    }
}
