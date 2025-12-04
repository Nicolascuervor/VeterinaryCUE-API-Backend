package co.cue.inventario_service.controller;
import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.BulkCategoriaResponseDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;
import co.cue.inventario_service.services.ICategoriaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController // Indica que esta clase expone endpoints REST
@RequestMapping("/api/inventario/categorias") // Ruta base para las categorías
@AllArgsConstructor // Genera un constructor con los atributos final
@Slf4j
public class CategoriaController {

    // Servicio encargado de la lógica de negocio de categorías
    private final ICategoriaService categoriaService;

    // GET: Obtiene todas las categorías activas
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = categoriaService.listAllActiveCategorias();
        return ResponseEntity.ok(categorias);
    }

    // GET: Endpoint público para obtener todas las categorías activas (para ecommerce)
    @GetMapping("/public")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategoriasPublicas() {
        List<CategoriaResponseDTO> categorias = categoriaService.listAllActiveCategorias();
        return ResponseEntity.ok(categorias);
    }

    // GET: Obtiene una categoría activa según su ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.findActiveById(id);
        return ResponseEntity.ok(categoria);
    }

    // POST: Crea una nueva categoría con los datos enviados
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO nuevaCategoria = categoriaService.createCategoria(requestDTO);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    // PUT: Actualiza una categoría existente identificada por ID
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO actualizada = categoriaService.updateCategoria(id, requestDTO);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE: Elimina una categoría (marcándola como inactiva o eliminándola)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST: Crea múltiples categorías de forma masiva
     * Acepta una lista de categorías para crear.
     * 
     * El endpoint procesa cada categoría individualmente, permitiendo que algunas se creen
     * exitosamente aunque otras fallen. Devuelve un resumen con las categorías creadas y los errores.
     * 
     * @param categorias Lista de categorías a crear
     * @return ResponseEntity con el resultado de la operación masiva
     */
    @PostMapping("/bulk")
    public ResponseEntity<BulkCategoriaResponseDTO> crearCategoriasMasivo(
            @Valid @RequestBody List<CategoriaRequestDTO> categorias) {
        
        try {
            if (categorias == null || categorias.isEmpty()) {
                log.warn("Intento de crear categorías masivas con lista null o vacía");
                return ResponseEntity.badRequest().build();
            }
            
            log.info("Recibida solicitud para crear {} categorías masivamente", categorias.size());
            BulkCategoriaResponseDTO resultado = categoriaService.crearCategoriasMasivo(categorias);
            
            // Si todas las categorías fallaron, devolver 400 Bad Request
            // Si algunas fallaron pero otras tuvieron éxito, devolver 201 Created
            if (resultado.getTotalExitosas() > 0) {
                log.info("Creación masiva completada: {} exitosas de {} procesadas", 
                        resultado.getTotalExitosas(), resultado.getTotalProcesadas());
                return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
            } else {
                log.warn("Todas las categorías fallaron en la creación masiva");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
            }
        } catch (HttpMessageNotReadableException e) {
            log.error("Error de deserialización JSON: {}", e.getMessage(), e);
            BulkCategoriaResponseDTO errorResponse = crearErrorResponse(
                "Error al deserializar el JSON. Verifique el formato de los datos. " + 
                "Detalle: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            log.error("Error inesperado al procesar creación masiva: {}", e.getMessage(), e);
            BulkCategoriaResponseDTO errorResponse = crearErrorResponse(
                "Error inesperado al procesar la solicitud: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Crea una respuesta de error estándar.
     */
    private BulkCategoriaResponseDTO crearErrorResponse(String mensajeError) {
        BulkCategoriaResponseDTO errorResponse = new BulkCategoriaResponseDTO();
        errorResponse.setCategoriasCreadas(new ArrayList<>());
        errorResponse.setErrores(new ArrayList<>());
        errorResponse.getErrores().add(new BulkCategoriaResponseDTO.ErrorCategoriaDTO(
            0, "Error general", mensajeError
        ));
        errorResponse.setTotalProcesadas(0);
        errorResponse.setTotalExitosas(0);
        errorResponse.setTotalFallidas(1);
        return errorResponse;
    }
}
