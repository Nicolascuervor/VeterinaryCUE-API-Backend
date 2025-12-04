package co.cue.inventario_service.services;


import co.cue.inventario_service.models.dtos.requestdtos.*;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {

    // Retorna todos los productos que están activos
    List<ProductoResponseDTO> listAllActiveProductos();

    // Busca un producto activo por su ID
    ProductoResponseDTO findActiveById(Long id);

    // Crea un producto de tipo Alimento
    ProductoResponseDTO createAlimento(AlimentoRequestDTO requestDTO);

    // Crea un producto de tipo Medicina
    ProductoResponseDTO createMedicina(MedicinaRequestDTO requestDTO);

    // Crea un producto de tipo Accesorio
    ProductoResponseDTO createAccesorio(AccesorioRequestDTO requestDTO);

    // Elimina un producto marcándolo como inactivo (eliminación lógica)
    void deleteProducto(Long id);

    // Actualiza la información general de un producto
    ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO dto);

    // Reduce el stock de una lista de productos según las cantidades indicadas
    void descontarStock(List<StockReductionDTO> items);

    // ========== MÉTODOS PARA ECOMMERCE ==========

    // Retorna todos los productos disponibles para venta (público, sin autenticación)
    List<ProductoResponseDTO> listarProductosDisponiblesParaVenta();

    // Retorna productos disponibles para venta con paginación
    Page<ProductoResponseDTO> listarProductosDisponiblesParaVenta(Pageable pageable);

    // Busca productos por nombre con paginación
    Page<ProductoResponseDTO> buscarProductosPorNombre(String nombre, Pageable pageable);

    // Busca productos por categoría con paginación
    Page<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId, Pageable pageable);

    // Búsqueda avanzada con múltiples filtros y paginación
    Page<ProductoResponseDTO> buscarProductosConFiltros(
            String nombre,
            Long categoriaId,
            Double precioMin,
            Double precioMax,
            Pageable pageable);

    // Sube una imagen para un producto y actualiza su campo foto
    String subirFotoProducto(Long productoId, org.springframework.web.multipart.MultipartFile file);

    // Crea múltiples productos de forma masiva
    co.cue.inventario_service.models.dtos.responsedtos.BulkProductoResponseDTO crearProductosMasivo(
            List<ProductoRequestDTO> productos);

    // Crea un producto según su tipo en una transacción separada (para creación masiva)
    ProductoResponseDTO crearProductoPorTipoTransaccional(ProductoRequestDTO dto);

}
