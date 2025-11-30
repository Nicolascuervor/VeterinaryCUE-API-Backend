package co.cue.inventario_service.controller;
import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;
import co.cue.inventario_service.services.ICategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de categorías de productos.
 * Expone endpoints para crear, leer, actualizar y eliminar categorías.
 * Estas categorías (ej. "Alimentos", "Medicamentos", "Juguetes") son fundamentales
 * para organizar el inventario y permitir el filtrado en el frontend.
 * Política de Acceso (definida en SecurityConfig):
 * - Operaciones de Lectura (GET): Públicas para cualquier usuario o visitante.
 * - Operaciones de Escritura (POST, PUT, DELETE): Restringidas exclusivamente a Administradores.
 */
@RestController
@RequestMapping("/api/inventario/categorias")
@AllArgsConstructor
public class CategoriaController {

    private final ICategoriaService categoriaService;

    /**
     * Recupera el listado completo de categorías activas.
     * Endpoint público utilizado para poblar menús de navegación o filtros de búsqueda
     * en la tienda virtual.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = categoriaService.listAllActiveCategorias();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Busca una categoría específica por su identificador.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.findActiveById(id);
        return ResponseEntity.ok(categoria);
    }

    /**
     * Crea una nueva categoría en el sistema.
     * Endpoint protegido (Admin). Recibe los datos básicos (nombre, descripción)
     * y delega la creación al servicio, el cual validará que el nombre no esté duplicado.
     */
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO nuevaCategoria = categoriaService.createCategoria(requestDTO);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de una categoría existente.
     * Endpoint protegido (Admin). Permite modificar el nombre o descripción.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO actualizada = categoriaService.updateCategoria(id, requestDTO);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina (lógicamente) una categoría del sistema.
     * Endpoint protegido (Admin). Marca la categoría como inactiva.
     * Nota de Integridad:
     * Si la categoría tiene productos asociados activos, el servicio podría lanzar
     * una excepción de integridad (DataIntegrityViolation) o impedir la acción
     * para no dejar productos huérfanos.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
