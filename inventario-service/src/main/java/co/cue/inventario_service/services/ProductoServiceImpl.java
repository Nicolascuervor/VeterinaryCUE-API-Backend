package co.cue.inventario_service.services;

import co.cue.inventario_service.mapper.ProductoMapper;
import co.cue.inventario_service.models.dtos.requestdtos.*;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.models.entities.*;
import co.cue.inventario_service.repository.CategoriaRepository;
import co.cue.inventario_service.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@AllArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    // Repositorio para acceder a productos
    private final ProductoRepository productoRepository;

    // Repositorio para acceder a categorías
    private final CategoriaRepository categoriaRepository;

    // Mapper para convertir entre entidades y DTOs
    private final ProductoMapper productoMapper;

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
}