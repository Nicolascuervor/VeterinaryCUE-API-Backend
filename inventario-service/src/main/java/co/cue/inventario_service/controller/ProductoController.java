package co.cue.inventario_service.controller;

import co.cue.inventario_service.models.dtos.requestdtos.AccesorioRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.AlimentoRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.MedicinaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.services.IProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/productos")
@AllArgsConstructor
public class ProductoController {

    private final IProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        List<ProductoResponseDTO> productos = productoService.listAllActiveProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.findActiveById(id);
        return ResponseEntity.ok(producto);
    }


    @PostMapping("/alimento")
    public ResponseEntity<ProductoResponseDTO> crearAlimento(@RequestBody AlimentoRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createAlimento(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PostMapping("/medicina")
    public ResponseEntity<ProductoResponseDTO> crearMedicina(@RequestBody MedicinaRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createMedicina(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PostMapping("/accesorio")
    public ResponseEntity<ProductoResponseDTO> crearAccesorio(@RequestBody AccesorioRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createAccesorio(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }
}
