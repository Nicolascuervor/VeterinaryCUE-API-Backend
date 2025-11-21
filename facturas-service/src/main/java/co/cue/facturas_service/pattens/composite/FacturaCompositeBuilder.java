package co.cue.facturas_service.pattens.composite;

import co.cue.facturas_service.models.dtos.kafka.PedidoItemEventDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FacturaCompositeBuilder {

    public List<ItemFactura> construirEstructura(Set<PedidoItemEventDTO> itemsKafKa) {
        List<ItemFactura> itemsEstructurados = new ArrayList<>();
        PaqueteProductos paquetePromocional = null;

        for (PedidoItemEventDTO itemDTO : itemsKafKa) {
            String nombreProducto = "Producto-" + itemDTO.getProductoId();

            ProductoIndividual producto = new ProductoIndividual(
                    nombreProducto,
                    itemDTO.getCantidad(),
                    itemDTO.getPrecioUnitario()
            );

            if (itemDTO.getCantidad() > 5) {
                if (paquetePromocional == null) {
                    paquetePromocional = new PaqueteProductos("Caja Mayorista", 1);
                    itemsEstructurados.add(paquetePromocional);
                }
                paquetePromocional.agregarComponente(producto);
            } else {

                itemsEstructurados.add(producto);
            }
        }
        return itemsEstructurados;
    }
}
