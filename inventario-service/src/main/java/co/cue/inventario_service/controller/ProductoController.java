package co.cue.inventario_service.controller;

import co.cue.inventario_service.models.dtos.requestdtos.*;
import co.cue.inventario_service.models.dtos.responsedtos.BulkProductoResponseDTO;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.services.IProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import java.util.List;

@RestController // Expone endpoints REST relacionados con productos
@RequestMapping("/api/inventario/productos") // Ruta base de productos
@AllArgsConstructor // Genera un constructor con los atributos final
@Slf4j
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

    // PUT: Actualiza un producto existente por su ID (genérico)
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequestDTO requestDTO) {

        ProductoResponseDTO actualizado = productoService.updateProducto(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    // PUT: Actualiza un producto de tipo Alimento por su ID
    @PutMapping("/alimento/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarAlimento(
            @PathVariable Long id,
            @RequestBody AlimentoRequestDTO requestDTO) {
        ProductoResponseDTO actualizado = productoService.updateAlimento(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    // PUT: Actualiza un producto de tipo Medicina por su ID
    @PutMapping("/medicina/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarMedicina(
            @PathVariable Long id,
            @RequestBody MedicinaRequestDTO requestDTO) {
        ProductoResponseDTO actualizado = productoService.updateMedicina(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    // PUT: Actualiza un producto de tipo Accesorio por su ID
    @PutMapping("/accesorio/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarAccesorio(
            @PathVariable Long id,
            @RequestBody AccesorioRequestDTO requestDTO) {
        ProductoResponseDTO actualizado = productoService.updateAccesorio(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    // PATCH: Actualiza el stock de un producto específico
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponseDTO> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        ProductoResponseDTO actualizado = productoService.actualizarStock(id, stock);
        return ResponseEntity.ok(actualizado);
    }

    // PATCH: Reactiva un producto que fue eliminado lógicamente
    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<ProductoResponseDTO> reactivarProducto(@PathVariable Long id) {
        ProductoResponseDTO reactivado = productoService.reactivarProducto(id);
        return ResponseEntity.ok(reactivado);
    }

    // PATCH: Actualiza la disponibilidad para venta de un producto
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<ProductoResponseDTO> actualizarDisponibilidadVenta(
            @PathVariable Long id,
            @RequestParam boolean disponibleParaVenta) {
        ProductoResponseDTO actualizado = productoService.actualizarDisponibilidadVenta(id, disponibleParaVenta);
        return ResponseEntity.ok(actualizado);
    }

    // POST: Descuenta el stock de una lista de productos
    @PostMapping("/stock/descontar")
    public ResponseEntity<Void> descontarStock(@RequestBody List<StockReductionDTO> items) {
        productoService.descontarStock(items);
        return ResponseEntity.noContent().build();
    }

    // ========== ENDPOINTS PÚBLICOS PARA ECOMMERCE ==========

    /**
     * GET: Obtiene todos los productos disponibles para venta (público, sin autenticación)
     * Endpoint para el catálogo del ecommerce
     */
    @GetMapping("/public/disponibles")
    public ResponseEntity<List<ProductoResponseDTO>> listarProductosDisponibles() {
        List<ProductoResponseDTO> productos = productoService.listarProductosDisponiblesParaVenta();
        return ResponseEntity.ok(productos);
    }

    /**
     * GET: Obtiene productos disponibles para venta con paginación (público)
     * Endpoint para el catálogo del ecommerce
     */
    @GetMapping("/public/disponibles/paginado")
    public ResponseEntity<Page<ProductoResponseDTO>> listarProductosDisponiblesPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ProductoResponseDTO> productos = productoService.listarProductosDisponiblesParaVenta(pageable);
        return ResponseEntity.ok(productos);
    }

    /**
     * GET: Busca productos por nombre con paginación (público)
     */
    @GetMapping("/public/buscar")
    public ResponseEntity<Page<ProductoResponseDTO>> buscarProductos(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ProductoResponseDTO> productos = productoService.buscarProductosPorNombre(nombre, pageable);
        return ResponseEntity.ok(productos);
    }

    /**
     * GET: Busca productos por categoría con paginación (público)
     */
    @GetMapping("/public/categoria/{categoriaId}")
    public ResponseEntity<Page<ProductoResponseDTO>> buscarProductosPorCategoria(
            @PathVariable Long categoriaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ProductoResponseDTO> productos = productoService.buscarProductosPorCategoria(categoriaId, pageable);
        return ResponseEntity.ok(productos);
    }

    /**
     * GET: Búsqueda avanzada con múltiples filtros (público)
     * Permite filtrar por nombre, categoría y rango de precios
     */
    @GetMapping("/public/filtrar")
    public ResponseEntity<Page<ProductoResponseDTO>> filtrarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ProductoResponseDTO> productos = productoService.buscarProductosConFiltros(
                nombre, categoriaId, precioMin, precioMax, pageable);
        return ResponseEntity.ok(productos);
    }

    /**
     * GET: Obtiene un producto disponible para venta por su ID (público)
     */
    @GetMapping("/public/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoDisponiblePorId(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.findActiveById(id);
        // Verificar que esté disponible para venta
        if (!producto.isDisponibleParaVenta()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    /**
     * POST: Sube una imagen para un producto
     * Solo administradores pueden subir imágenes
     */
    @PostMapping(value = "/{id}/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirFotoProducto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                log.warn("Intento de subir archivo vacío o null para producto {}", id);
                return ResponseEntity.badRequest().body("El archivo no puede estar vacío");
            }
            
            log.info("Intentando subir foto para producto {}", id);
            String fotoUrl = productoService.subirFotoProducto(id, file);
            log.info("Foto subida exitosamente para producto {}: {}", id, fotoUrl);
            return ResponseEntity.ok(fotoUrl);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al subir foto para producto {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (jakarta.persistence.EntityNotFoundException e) {
            log.error("Producto no encontrado al subir foto: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error inesperado al subir foto para producto {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir la imagen: " + e.getMessage());
        }
    }

    /**
     * POST: Crea múltiples productos de forma masiva
     * Acepta una lista de productos de cualquier tipo (Alimento, Medicina, Accesorio).
     * Cada producto debe incluir el campo "tipoProducto" para identificar su tipo.
     * 
     * El endpoint procesa cada producto individualmente, permitiendo que algunos se creen
     * exitosamente aunque otros fallen. Devuelve un resumen con los productos creados y los errores.
     * 
     * @param productos Lista de productos a crear (pueden ser de diferentes tipos)
     * @return ResponseEntity con el resultado de la operación masiva
     */
    @PostMapping("/bulk")
    public ResponseEntity<BulkProductoResponseDTO> crearProductosMasivo(
            @Valid @RequestBody List<ProductoRequestDTO> productos) {
        
        try {
            if (productos == null || productos.isEmpty()) {
                log.warn("Intento de crear productos masivos con lista null o vacía");
                return ResponseEntity.badRequest().build();
            }
            
            log.info("Recibida solicitud para crear {} productos masivamente", productos.size());
            BulkProductoResponseDTO resultado = productoService.crearProductosMasivo(productos);
            
            // Si todos los productos fallaron, devolver 400 Bad Request
            // Si algunos fallaron pero otros tuvieron éxito, devolver 201 Created
            if (resultado.getTotalExitosos() > 0) {
                log.info("Creación masiva completada: {} exitosos de {} procesados", 
                        resultado.getTotalExitosos(), resultado.getTotalProcesados());
                return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
            } else {
                log.warn("Todos los productos fallaron en la creación masiva");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
            }
        } catch (HttpMessageNotReadableException e) {
            log.error("Error de deserialización JSON: {}", e.getMessage(), e);
            BulkProductoResponseDTO errorResponse = crearErrorResponse(
                "Error al deserializar el JSON. Verifique que cada producto tenga el campo 'tipoProducto' y que sea válido (ALIMENTO, MEDICINA, ACCESORIO). " + 
                "Detalle: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            log.error("Error inesperado al procesar creación masiva: {}", e.getMessage(), e);
            BulkProductoResponseDTO errorResponse = crearErrorResponse(
                "Error inesperado al procesar la solicitud: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Crea una respuesta de error estándar.
     */
    private BulkProductoResponseDTO crearErrorResponse(String mensajeError) {
        BulkProductoResponseDTO errorResponse = new BulkProductoResponseDTO();
        errorResponse.setProductosCreados(new ArrayList<>());
        errorResponse.setErrores(new ArrayList<>());
        errorResponse.getErrores().add(new BulkProductoResponseDTO.ErrorProductoDTO(
            0, "Error general", "ERROR", mensajeError
        ));
        errorResponse.setTotalProcesados(0);
        errorResponse.setTotalExitosos(0);
        errorResponse.setTotalFallidos(1);
        return errorResponse;
    }

}
