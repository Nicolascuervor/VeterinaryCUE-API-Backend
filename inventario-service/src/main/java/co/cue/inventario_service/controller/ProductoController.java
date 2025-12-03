package co.cue.inventario_service.controller;

import co.cue.inventario_service.models.dtos.requestdtos.*;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.services.IProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Expone endpoints REST relacionados con productos
@RequestMapping("/api/inventario/productos") // Ruta base de productos
@AllArgsConstructor // Genera un constructor con los atributos final
public class ProductoController {

    // Servicio con la lógica de negocio de productos
    private final IProductoService productoService;

    // GET: Obtiene todos los productos activos
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        List<ProductoResponseDTO> productos = productoService.listAllActiveProductos();
        return ResponseEntity.ok(productos);
    }

    // GET: Obtiene un producto activo por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.findActiveById(id);
        return ResponseEntity.ok(producto);
    }

    // POST: Crea un producto tipo Alimento
    @PostMapping("/alimento")
    public ResponseEntity<ProductoResponseDTO> crearAlimento(@RequestBody AlimentoRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createAlimento(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // POST: Crea un producto tipo Medicina
    @PostMapping("/medicina")
    public ResponseEntity<ProductoResponseDTO> crearMedicina(@RequestBody MedicinaRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createMedicina(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // POST: Crea un producto tipo Accesorio
    @PostMapping("/accesorio")
    public ResponseEntity<ProductoResponseDTO> crearAccesorio(@RequestBody AccesorioRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createAccesorio(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // DELETE: Elimina lógicamente un producto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    // PUT: Actualiza un producto existente por su ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequestDTO requestDTO) {

        ProductoResponseDTO actualizado = productoService.updateProducto(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    // POST: Descuenta el stock de una lista de productos
    @PostMapping("/stock/descontar")
    public ResponseEntity<Void> descontarStock(@RequestBody List<StockReductionDTO> items) {
        productoService.descontarStock(items);
        return ResponseEntity.noContent().build();
    }


}
