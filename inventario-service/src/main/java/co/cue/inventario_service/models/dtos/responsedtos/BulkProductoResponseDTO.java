package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO de respuesta para operaciones de creación masiva de productos.
 * Contiene información sobre los productos creados exitosamente y los errores ocurridos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkProductoResponseDTO {
    
    /**
     * Lista de productos creados exitosamente.
     */
    private List<ProductoResponseDTO> productosCreados;
    
    /**
     * Lista de errores ocurridos durante la creación.
     * Cada error contiene el índice del producto que falló y el mensaje de error.
     */
    private List<ErrorProductoDTO> errores;
    
    /**
     * Total de productos procesados.
     */
    private int totalProcesados;
    
    /**
     * Total de productos creados exitosamente.
     */
    private int totalExitosos;
    
    /**
     * Total de productos que fallaron.
     */
    private int totalFallidos;
    
    /**
     * DTO interno para representar errores individuales.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorProductoDTO {
        /**
         * Índice del producto en la lista original que falló.
         */
        private int indice;
        
        /**
         * Nombre del producto que falló (si estaba disponible).
         */
        private String nombreProducto;
        
        /**
         * Tipo de producto que falló (ALIMENTO, MEDICINA, ACCESORIO).
         */
        private String tipoProducto;
        
        /**
         * Mensaje de error descriptivo.
         */
        private String mensajeError;
    }
}

