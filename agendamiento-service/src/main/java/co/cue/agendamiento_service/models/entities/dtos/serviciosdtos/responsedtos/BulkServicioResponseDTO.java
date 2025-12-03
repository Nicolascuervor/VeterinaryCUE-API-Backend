package co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO de respuesta para operaciones de creación masiva de servicios.
 * Contiene información sobre los servicios creados exitosamente y los errores ocurridos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkServicioResponseDTO {
    
    /**
     * Lista de servicios creados exitosamente.
     */
    private List<ServicioResponseDTO> serviciosCreados;
    
    /**
     * Lista de errores ocurridos durante la creación.
     * Cada error contiene el índice del servicio que falló y el mensaje de error.
     */
    private List<ErrorServicioDTO> errores;
    
    /**
     * Total de servicios procesados.
     */
    private int totalProcesados;
    
    /**
     * Total de servicios creados exitosamente.
     */
    private int totalExitosos;
    
    /**
     * Total de servicios que fallaron.
     */
    private int totalFallidos;
    
    /**
     * DTO interno para representar errores individuales.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorServicioDTO {
        /**
         * Índice del servicio en la lista original que falló.
         */
        private int indice;
        
        /**
         * Nombre del servicio que falló (si estaba disponible).
         */
        private String nombreServicio;
        
        /**
         * Tipo de servicio que falló.
         */
        private String tipoServicio;
        
        /**
         * Mensaje de error descriptivo.
         */
        private String mensajeError;
    }
}

