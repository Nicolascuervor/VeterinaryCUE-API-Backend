package co.cue.facturas_service.pattens.composite;

import co.cue.facturas_service.models.dtos.kafka.PedidoItemEventDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
// Marca esta clase como un componente de servicio dentro del contexto de Spring.
@Service
public class FacturaCompositeBuilder {
    /**
     * Construye la estructura de ítems de factura usando el patrón Composite.
     * Convierte ítems recibidos desde Kafka en objetos de dominio (ProductoIndividual o PaqueteProductos).
     *
     * @param itemsKafKa Conjunto de ítems provenientes del evento Kafka.
     * @return Lista estructurada de ItemFactura lista para usar en el cálculo de factura.
     */
    public List<ItemFactura> construirEstructura(Set<PedidoItemEventDTO> itemsKafKa) {

        // Lista final donde se almacenarán los ítems generados (productos sueltos o paquetes).
        List<ItemFactura> itemsEstructurados = new ArrayList<>();

        // Paquete promocional que agrupará productos que cumplan cierta condición.
        PaqueteProductos paquetePromocional = null;

        // Recorre cada ítem recibido desde Kafka.
        for (PedidoItemEventDTO itemDTO : itemsKafKa) {

            // Simulación del nombre del producto basado en su ID
            String nombreProducto = "Producto-" + itemDTO.getProductoId();


            // Crea un objeto ProductoIndividual (componente del Composite)
            ProductoIndividual producto = new ProductoIndividual(
                    nombreProducto,
                    itemDTO.getCantidad(),
                    itemDTO.getPrecioUnitario()
            );

            // Si la cantidad excede 5 unidades, se agrupa en un paquete promocional.
            if (itemDTO.getCantidad() > 5) {


                // Si todavía no existe un paquete, se crea uno y se agrega a la lista.
                if (paquetePromocional == null) {
                    paquetePromocional = new PaqueteProductos("Caja Mayorista", 1);
                    itemsEstructurados.add(paquetePromocional);
                }

                // Agrega el producto dentro del paquete.
                paquetePromocional.agregarComponente(producto);
            } else {

                // Si no cumple la condición, simplemente se agrega como producto individual.
                itemsEstructurados.add(producto);
            }
        }

        // Si no cumple la condición, simplemente se agrega como producto individual.
        return itemsEstructurados;
    }
}
