package co.cue.inventario_service.models.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO de respuesta para operaciones de creación masiva de categorías.
 * Contiene información sobre las categorías creadas exitosamente y los errores ocurridos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkCategoriaResponseDTO {
    
    /**
     * Lista de categorías creadas exitosamente.
     */
    private List<CategoriaResponseDTO> categoriasCreadas;
    
    /**
     * Lista de errores ocurridos durante la creación.
     * Cada error contiene el índice de la categoría que falló y el mensaje de error.
     */
    private List<ErrorCategoriaDTO> errores;
    
    /**
     * Total de categorías procesadas.
     */
    private int totalProcesadas;
    
    /**
     * Total de categorías creadas exitosamente.
     */
    private int totalExitosas;
    
    /**
     * Total de categorías que fallaron.
     */
    private int totalFallidas;
    
    /**
     * DTO interno para representar errores individuales.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorCategoriaDTO {
        /**
         * Índice de la categoría en la lista original que falló.
         */
        private int indice;
        
        /**
         * Nombre de la categoría que falló (si estaba disponible).
         */
        private String nombreCategoria;
        
        /**
         * Mensaje de error descriptivo.
         */
        private String mensajeError;
    }
}

