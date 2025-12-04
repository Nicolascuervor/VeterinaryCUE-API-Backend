package co.cue.inventario_service.services;

import co.cue.inventario_service.mapper.ProductoMapper;
import co.cue.inventario_service.models.dtos.requestdtos.*;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.models.entities.*;
import co.cue.inventario_service.repository.CategoriaRepository;
import co.cue.inventario_service.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import co.cue.inventario_service.config.FileStorageException;
import co.cue.inventario_service.models.dtos.responsedtos.BulkProductoResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductoServiceImpl implements IProductoService {

    // Repositorio para acceder a productos
    private final ProductoRepository productoRepository;

    // Repositorio para acceder a categorías
    private final CategoriaRepository categoriaRepository;

    // Mapper para convertir entre entidades y DTOs
    private final ProductoMapper productoMapper;

    // Contexto de aplicación para obtener el proxy y evitar dependencias circulares
    private final ApplicationContext applicationContext;

    // Directorio donde se guardarán las imágenes de productos
    @Value("${file.upload-dir:uploads/productos}")
    private String uploadDir;

    // Constructor con ApplicationContext para evitar dependencias circulares
    public ProductoServiceImpl(ProductoRepository productoRepository,
                               CategoriaRepository categoriaRepository,
                               ProductoMapper productoMapper,
                               ApplicationContext applicationContext) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoMapper = productoMapper;
        this.applicationContext = applicationContext;
    }

    private Path getRootLocation() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Obtiene el proxy del servicio para permitir llamadas transaccionales.
     */
    private IProductoService getSelf() {
        return applicationContext.getBean(IProductoService.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listAllActiveProductos() {
        // Retorna todos los productos activos mapeados a DTO
        return productoRepository.findAllByActivoTrue()
                .stream()
                .map(productoMapper::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO findActiveById(Long id) {

        // Busca producto activo y lo convierte a DTO
        Producto producto = findProductoActivoById(id);
        return productoMapper.mapToResponseDTO(producto);
    }

    @Override
    @Transactional
    public ProductoResponseDTO createAlimento(AlimentoRequestDTO requestDTO) {
        // Crea alimento usando el método genérico de creación
        Alimento alimento = (Alimento) createProducto(requestDTO, productoMapper.mapToEntity(requestDTO));
        return productoMapper.mapToResponseDTO(alimento);
    }

    @Override
    @Transactional
    public ProductoResponseDTO createMedicina(MedicinaRequestDTO requestDTO) {
        // Crea medicina usando el método genérico de creación
        Medicina medicina = (Medicina) createProducto(requestDTO, productoMapper.mapToEntity(requestDTO));
        return productoMapper.mapToResponseDTO(medicina);
    }

    @Override
    @Transactional
    public ProductoResponseDTO createAccesorio(AccesorioRequestDTO requestDTO) {
        // Crea accesorio usando el método genérico de creación
        Accesorio accesorio = (Accesorio) createProducto(requestDTO, productoMapper.mapToEntity(requestDTO));
        return productoMapper.mapToResponseDTO(accesorio);
    }

    @Override
    @Transactional
    public void deleteProducto(Long id) {
        // Baja lógica de un producto
        Producto producto = findProductoActivoById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
    // -----------------------------------------------------------
    // MÉTODO GENÉRICO PARA CREAR PRODUCTOS
    // -----------------------------------------------------------

    private Producto createProducto(ProductoRequestDTO dto, Producto entity) {
        // Validación de nombre duplicado
        if (productoRepository.existsByNombreAndActivoTrue(dto.getNombre())) {
            throw new DataIntegrityViolationException("El nombre de producto '" + dto.getNombre() + "' ya existe.");
        }


        // Obtener categoría y verificar que esté activa
        Categoria categoria = categoriaRepository.findByIdAndActivoTrue(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));


        // Asignar categoría al producto
        entity.setCategoria(categoria);

        // Guardar producto
        return productoRepository.save(entity);
    }

    // -------------------------------
    // BUSCAR PRODUCTO ACTIVO POR ID
    // -------------------------------
    private Producto findProductoActivoById(Long id) {
        return productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado o inactivo con ID: " + id));
    }

    @Override
    @Transactional
    public ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO dto) {
        // Buscar producto existente
        Producto producto = findProductoActivoById(id);

        // Actualizar campos comunes del producto
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setFoto(dto.getFoto());
        producto.setPrecio(dto.getPrecio());
        producto.setStockActual(dto.getStockActual());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setUbicacion(dto.getUbicacion());
        producto.setDisponibleParaVenta(dto.isDisponibleParaVenta());

        // Si se envió una nueva categoría, actualizarla
        if (dto.getCategoriaId() != null) {
            Categoria nuevaCategoria = categoriaRepository.findByIdAndActivoTrue(dto.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
            producto.setCategoria(nuevaCategoria);
        }
        // Lógica de actualización por tipo de producto
        if (producto instanceof Alimento alimento && dto instanceof AlimentoRequestDTO alimentoDTO) {
            alimento.setTipoMascota(alimentoDTO.getTipoMascota());
            alimento.setPesoEnKg(alimentoDTO.getPesoEnKg());

        } else if (producto instanceof Medicina medicina && dto instanceof MedicinaRequestDTO medicinaDTO) {
            medicina.setComposicion(medicinaDTO.getComposicion());
            medicina.setDosisRecomendada(medicinaDTO.getDosisRecomendada());

        } else if (producto instanceof Accesorio accesorio && dto instanceof AccesorioRequestDTO accesorioDTO) {
            accesorio.setMaterial(accesorioDTO.getMaterial());
            accesorio.setTamanio(accesorioDTO.getTamanio());

        } else {
            // Si el tipo no coincide, se lanza error
            throw new IllegalArgumentException("El tipo de producto enviado no coincide con el producto guardado en base de datos.");
        }

        // Guardar y retornar actualizado
        Producto actualizado = productoRepository.save(producto);
        return productoMapper.mapToResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public ProductoResponseDTO updateAlimento(Long id, AlimentoRequestDTO requestDTO) {
        Producto producto = findProductoActivoById(id);
        if (!(producto instanceof Alimento)) {
            throw new IllegalArgumentException("El producto con ID " + id + " no es de tipo Alimento");
        }
        return updateProducto(id, requestDTO);
    }

    @Override
    @Transactional
    public ProductoResponseDTO updateMedicina(Long id, MedicinaRequestDTO requestDTO) {
        Producto producto = findProductoActivoById(id);
        if (!(producto instanceof Medicina)) {
            throw new IllegalArgumentException("El producto con ID " + id + " no es de tipo Medicina");
        }
        return updateProducto(id, requestDTO);
    }

    @Override
    @Transactional
    public ProductoResponseDTO updateAccesorio(Long id, AccesorioRequestDTO requestDTO) {
        Producto producto = findProductoActivoById(id);
        if (!(producto instanceof Accesorio)) {
            throw new IllegalArgumentException("El producto con ID " + id + " no es de tipo Accesorio");
        }
        return updateProducto(id, requestDTO);
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizarStock(Long id, Integer nuevoStock) {
        if (nuevoStock == null || nuevoStock < 0) {
            throw new IllegalArgumentException("El stock debe ser un número mayor o igual a 0");
        }
        Producto producto = findProductoActivoById(id);
        producto.setStockActual(nuevoStock);
        Producto actualizado = productoRepository.save(producto);
        return productoMapper.mapToResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public ProductoResponseDTO reactivarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Producto no encontrado con ID: " + id));
        
        if (producto.isActivo()) {
            throw new IllegalStateException("El producto con ID " + id + " ya está activo");
        }
        
        producto.setActivo(true);
        Producto reactivado = productoRepository.save(producto);
        return productoMapper.mapToResponseDTO(reactivado);
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizarDisponibilidadVenta(Long id, boolean disponibleParaVenta) {
        Producto producto = findProductoActivoById(id);
        producto.setDisponibleParaVenta(disponibleParaVenta);
        Producto actualizado = productoRepository.save(producto);
        return productoMapper.mapToResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void descontarStock(List<StockReductionDTO> items) {
        // Recorre los elementos y descuenta el stock de cada producto
        for (StockReductionDTO item : items) {
            Producto producto = findProductoActivoById(item.getProductoId());

            int nuevoStock = producto.getStockActual() - item.getCantidad();

            // Validar que el stock no quede negativo
            if (nuevoStock < 0) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStockActual(nuevoStock);
            productoRepository.save(producto);
        }
    }

    // ========== MÉTODOS PARA ECOMMERCE ==========

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductosDisponiblesParaVenta() {
        return productoRepository.findAllByActivoTrueAndDisponibleParaVentaTrue()
                .stream()
                .map(productoMapper::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listarProductosDisponiblesParaVenta(Pageable pageable) {
        return productoRepository.findAllByActivoTrueAndDisponibleParaVentaTrue(pageable)
                .map(productoMapper::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> buscarProductosPorNombre(String nombre, Pageable pageable) {
        return productoRepository.buscarProductosPorNombre(nombre, pageable)
                .map(productoMapper::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId, Pageable pageable) {
        return productoRepository.buscarProductosPorCategoria(categoriaId, pageable)
                .map(productoMapper::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> buscarProductosConFiltros(
            String nombre,
            Long categoriaId,
            Double precioMin,
            Double precioMax,
            Pageable pageable) {
        return productoRepository.buscarProductosConFiltros(nombre, categoriaId, precioMin, precioMax, pageable)
                .map(productoMapper::mapToResponseDTO);
    }

    @Override
    @Transactional
    public String subirFotoProducto(Long productoId, MultipartFile file) {
        // A. Validaciones básicas
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No se puede subir un archivo vacío");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene un nombre válido");
        }

        // Validar que sea una imagen
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // B. Crear directorio si no existe
        Path rootLocation = getRootLocation();
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (Exception e) {
            throw new FileStorageException("No se pudo inicializar la carpeta de uploads", e);
        }

        // C. Generar nombre único
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
        }
        String filename = UUID.randomUUID().toString() + extension;
        Path destinationFile = rootLocation.resolve(Paths.get(filename))
                .normalize().toAbsolutePath();

        // D. Guardar el archivo
        try {
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileStorageException("Fallo al guardar el archivo físico", e);
        }

        // E. Actualizar la Entidad Producto
        Producto producto = findProductoActivoById(productoId);

        // Construimos la URL relativa
        String relativePath = "/api/inventario/uploads/" + filename;

        producto.setFoto(relativePath);
        productoRepository.save(producto);

        return relativePath;
    }

    @Override
    public BulkProductoResponseDTO crearProductosMasivo(List<ProductoRequestDTO> productos) {
        log.info("Iniciando creación masiva de {} productos", productos.size());
        
        List<ProductoResponseDTO> productosCreados = new ArrayList<>();
        List<BulkProductoResponseDTO.ErrorProductoDTO> errores = new ArrayList<>();
        
        for (int i = 0; i < productos.size(); i++) {
            ProductoRequestDTO productoDTO = productos.get(i);
            String nombreProducto = "Sin nombre";
            String tipoProducto = "DESCONOCIDO";
            
            try {
                // Validar que el DTO no sea null
                if (productoDTO == null) {
                    throw new IllegalArgumentException("El producto en el índice " + i + " es null");
                }
                
                nombreProducto = productoDTO.getNombre() != null ? productoDTO.getNombre() : "Sin nombre";
                tipoProducto = obtenerTipoProducto(productoDTO);
                
                // Validar campos requeridos
                validarProductoDTO(productoDTO, i);
                
                // Crear producto en su propia transacción usando el proxy
                ProductoResponseDTO productoCreado = getSelf().crearProductoPorTipoTransaccional(productoDTO);
                productosCreados.add(productoCreado);
                log.debug("Producto {} (índice {}) creado exitosamente: {}", nombreProducto, i, productoCreado.getId());
            } catch (Exception e) {
                String mensajeError = extraerMensajeError(e);
                errores.add(new BulkProductoResponseDTO.ErrorProductoDTO(i, nombreProducto, tipoProducto, mensajeError));
                log.error("Error al crear producto {} (índice {}): {}", nombreProducto, i, mensajeError, e);
            }
        }
        
        int totalProcesados = productos.size();
        int totalExitosos = productosCreados.size();
        int totalFallidos = errores.size();
        
        log.info("Creación masiva completada: {} procesados, {} exitosos, {} fallidos", 
                totalProcesados, totalExitosos, totalFallidos);
        
        return new BulkProductoResponseDTO(productosCreados, errores, totalProcesados, totalExitosos, totalFallidos);
    }
    
    /**
     * Valida que el DTO tenga los campos requeridos.
     */
    private void validarProductoDTO(ProductoRequestDTO dto, int indice) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es requerido (índice " + indice + ")");
        }
        if (dto.getPrecio() == null || dto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0 (índice " + indice + ")");
        }
        if (dto.getStockActual() == null || dto.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock actual debe ser mayor o igual a 0 (índice " + indice + ")");
        }
        if (dto.getCategoriaId() == null) {
            throw new IllegalArgumentException("El ID de categoría es requerido (índice " + indice + ")");
        }
    }
    
    /**
     * Crea un producto según su tipo en una transacción separada.
     * Esto permite que cada producto se cree independientemente.
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductoResponseDTO crearProductoPorTipoTransaccional(ProductoRequestDTO dto) {
        return crearProductoPorTipo(dto);
    }
    
    /**
     * Extrae un mensaje de error legible de una excepción.
     */
    private String extraerMensajeError(Exception e) {
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            return e.getMessage();
        }
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            return e.getCause().getMessage();
        }
        return "Error desconocido: " + e.getClass().getSimpleName();
    }

    /**
     * Crea un producto según su tipo específico.
     * Usa el proxy del servicio para garantizar que las transacciones funcionen correctamente.
     */
    private ProductoResponseDTO crearProductoPorTipo(ProductoRequestDTO dto) {
        IProductoService self = getSelf();
        if (dto instanceof AlimentoRequestDTO alimentoDTO) {
            return self.createAlimento(alimentoDTO);
        } else if (dto instanceof MedicinaRequestDTO medicinaDTO) {
            return self.createMedicina(medicinaDTO);
        } else if (dto instanceof AccesorioRequestDTO accesorioDTO) {
            return self.createAccesorio(accesorioDTO);
        } else {
            throw new IllegalArgumentException("Tipo de producto no reconocido: " + dto.getClass().getSimpleName());
        }
    }

    /**
     * Obtiene el tipo de producto como String para logging y reportes.
     */
    private String obtenerTipoProducto(ProductoRequestDTO dto) {
        if (dto instanceof AlimentoRequestDTO) {
            return "ALIMENTO";
        } else if (dto instanceof MedicinaRequestDTO) {
            return "MEDICINA";
        } else if (dto instanceof AccesorioRequestDTO) {
            return "ACCESORIO";
        } else {
            return "DESCONOCIDO";
        }
    }
}