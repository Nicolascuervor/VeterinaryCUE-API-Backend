package co.cue.inventario_service.controller;

import co.cue.inventario_service.models.dtos.requestdtos.*;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.services.IProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión centralizada de productos en el inventario.
 * Este controlador actúa como el punto de entrada para todas las operaciones relacionadas
 * con los productos (Alimentos, Medicamentos, Accesorios, Kits).
 * Responsabilidades principales:
 * - Exponer el catálogo de productos para consulta pública.
 * - Permitir la creación polimórfica de productos específicos (cada tipo tiene su endpoint).
 * - Gestionar actualizaciones y eliminación lógica (Soft Delete).
 * - Proveer un endpoint crítico para la gestión de stock (llamado por el servicio de pedidos).
 * Política de Acceso (según SecurityConfig):
 * - Lectura (GET): Pública.
 * - Escritura (POST/PUT/DELETE): Solo Administradores.
 * - Descuento de Stock: Abierto (para comunicación entre microservicios).
 */
@RestController
@RequestMapping("/api/inventario/productos")
@AllArgsConstructor
public class ProductoController {

    private final IProductoService productoService;

    /**
     * Recupera la lista completa de productos activos en el inventario.
     * Endpoint público utilizado para mostrar el catálogo general en la tienda.
     * Devuelve una lista polimórfica de DTOs (puede contener Alimentos, Medicinas, etc.).
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        List<ProductoResponseDTO> productos = productoService.listAllActiveProductos();
        return ResponseEntity.ok(productos);
    }

    /**
     * Busca un producto específico por su ID.
     * Endpoint público. Retorna el detalle completo del producto, incluyendo
     * atributos específicos de su tipo (ej. "dosis" si es Medicina).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.findActiveById(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * Crea un nuevo producto de tipo ALIMENTO.
     * Endpoint protegido (Admin). Recibe atributos específicos como "pesoEnKg" y "tipoMascota".
     */
    @PostMapping("/alimento")
    public ResponseEntity<ProductoResponseDTO> crearAlimento(@RequestBody AlimentoRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createAlimento(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    /**
     * Crea un nuevo producto de tipo MEDICINA.
     * Endpoint protegido (Admin). Recibe atributos específicos como "dosisRecomendada" y "composicion".
     */
    @PostMapping("/medicina")
    public ResponseEntity<ProductoResponseDTO> crearMedicina(@RequestBody MedicinaRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createMedicina(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    /**
     * Crea un nuevo producto de tipo ACCESORIO.
     * Endpoint protegido (Admin). Recibe atributos específicos como "material" y "tamanio".
     */
    @PostMapping("/accesorio")
    public ResponseEntity<ProductoResponseDTO> crearAccesorio(@RequestBody AccesorioRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.createAccesorio(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    /**
     * Realiza una eliminación lógica (desactivación) de un producto.
     * Endpoint protegido (Admin). El producto no se borra físicamente para mantener
     * la integridad referencial con pedidos históricos, pero deja de aparecer en el catálogo público.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza los datos de un producto existente.
     * Endpoint protegido (Admin). Este método es polimórfico en la capa de servicio:
     * dependiendo del tipo real del producto en base de datos y del DTO enviado,
     * actualizará los campos correspondientes.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequestDTO requestDTO) {

        ProductoResponseDTO actualizado = productoService.updateProducto(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Reduce el stock de un lote de productos.
     * Endpoint Operativo. Este método es invocado principalmente por el microservicio de Pedidos
     * (pedidos-service) cuando se confirma una compra exitosa.
     * Nota de Seguridad: Aunque en SecurityConfig está configurado como permitAll (para facilitar
     * la comunicación S2S), es una operación crítica que modifica el estado del inventario.
     */
    @PostMapping("/stock/descontar")
    public ResponseEntity<Void> descontarStock(@RequestBody List<StockReductionDTO> items) {
        productoService.descontarStock(items);
        return ResponseEntity.noContent().build();
    }
}