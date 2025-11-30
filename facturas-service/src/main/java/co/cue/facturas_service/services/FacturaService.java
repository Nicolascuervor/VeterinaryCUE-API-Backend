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
// Marca esta clase como un servicio de Spring, encargado de la lógica relacionada con facturas.
@Service

// Genera un constructor con todos los campos finales mediante Lombok.
@AllArgsConstructor
public class FacturaService {
    // Repositorio para acceder a la tabla de facturas en la base de datos.
    private final FacturaRepository facturaRepository;


    // Obtiene todas las facturas y las convierte a DTO.
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> listarTodas() {

        // Recupera todas las facturas -> convierte cada una con mapToDTO -> retorna como lista.
        return facturaRepository.findAll().stream().map(this::mapToDTO).toList();
    }


    // Lista todas las facturas asociadas a un usuario específico.
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> listarPorUsuario(Long usuarioId) {
        return facturaRepository.findByUsuarioId(usuarioId).stream().map(this::mapToDTO).toList();
    }


    // Busca una factura por su ID. Si no existe, lanza excepción.
    @Transactional(readOnly = true)
    public FacturaResponseDTO buscarPorId(Long id) {
        return facturaRepository.findById(id)
                .map(this::mapToDTO)  // Si la encuentra, la transforma a DTO
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada"));
    }


    // Método privado encargado de convertir una entidad Factura a su DTO correspondiente.
    private FacturaResponseDTO mapToDTO(Factura f) {
        // Se crea el DTO destino.
        FacturaResponseDTO dto = new FacturaResponseDTO();


        // Se asignan los campos comunes a todas las facturas.
        dto.setId(f.getId());
        dto.setNumFactura(f.getNumFactura());
        dto.setFechaEmision(f.getFechaEmision());
        dto.setTotal(f.getTotal());
        dto.setEstado(f.getEstadoFactura().name());
        dto.setMetodoPago(f.getMetodoPago().name());


        // Si la factura es de tipo PRODUCTOS, entonces se deben mapear sus líneas.
        if (f instanceof FacturaProductos fp) {

            // Convertimos cada línea a su DTO correspondiente.
            dto.setLineas(fp.getLineas().stream().map(l -> {
                // DTO de la línea
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
