package co.cue.inventario_service.models.dtos.responsedtos;

/**
 * Objeto de Transferencia de Datos (DTO) para la respuesta de productos tipo KIT.
 *
 * Esta clase representa la proyección de salida para los productos compuestos (Kits)
 * en el catálogo. Extiende de ProductoResponseDTO para heredar todos los atributos
 * comerciales estándar (nombre, precio, stock, categoría).
 *
 * Nota de Diseño:
 * Actualmente, esta clase no define atributos adicionales propios. Sin embargo,
 * su existencia es fundamental para el mecanismo de polimorfismo de la API:
 * permite que el cliente (Frontend) identifique que este ítem es un "KIT" (a través
 * del campo discriminador 'tipoProducto') y pueda, por ejemplo, mostrar un icono
 * de "paquete" o habilitar una vista de detalle diferente.
 *
 * En futuras versiones, aquí se podría incluir la lista de 'componentes' del kit
 * si se desea mostrar el contenido del paquete en el listado general.
 */
public class KitProductoResponseDTO extends ProductoResponseDTO {
    // Intencionalmente vacío.
    // Hereda toda la estructura de datos de la clase padre.
}
