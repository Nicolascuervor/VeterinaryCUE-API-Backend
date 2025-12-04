package co.cue.inventario_service.controller;
import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;
import co.cue.inventario_service.services.ICategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase expone endpoints REST
@RequestMapping("/api/inventario/categorias") // Ruta base para las categorías
@AllArgsConstructor // Genera un constructor con los atributos final
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
}
