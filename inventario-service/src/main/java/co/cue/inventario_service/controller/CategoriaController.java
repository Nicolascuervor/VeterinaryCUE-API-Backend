package co.cue.inventario_service.controller;
import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;
import co.cue.inventario_service.services.ICategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/categorias")
@AllArgsConstructor
public class CategoriaController {

    private final ICategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = categoriaService.listAllActiveCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.findActiveById(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO nuevaCategoria = categoriaService.createCategoria(requestDTO);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO actualizada = categoriaService.updateCategoria(id, requestDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
