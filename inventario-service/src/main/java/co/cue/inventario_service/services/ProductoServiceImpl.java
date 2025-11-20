package co.cue.inventario_service.services;

import co.cue.inventario_service.mapper.ProductoMapper;
import co.cue.inventario_service.models.dtos.requestdtos.AccesorioRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.AlimentoRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.MedicinaRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.ProductoRequestDTO;
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
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listAllActiveProductos() {
        return productoRepository.findAllByActivoTrue()
                .stream()
                .map(productoMapper::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO findActiveById(Long id) {
        Producto producto = findProductoActivoById(id);
        return productoMapper.mapToResponseDTO(producto);
    }

    @Override
    @Transactional
    public ProductoResponseDTO createAlimento(AlimentoRequestDTO requestDTO) {
        Alimento alimento = (Alimento) createProducto(requestDTO, productoMapper.mapToEntity(requestDTO));
        return productoMapper.mapToResponseDTO(alimento);
    }

    @Override
    @Transactional
    public ProductoResponseDTO createMedicina(MedicinaRequestDTO requestDTO) {
        Medicina medicina = (Medicina) createProducto(requestDTO, productoMapper.mapToEntity(requestDTO));
        return productoMapper.mapToResponseDTO(medicina);
    }

    @Override
    @Transactional
    public ProductoResponseDTO createAccesorio(AccesorioRequestDTO requestDTO) {
        Accesorio accesorio = (Accesorio) createProducto(requestDTO, productoMapper.mapToEntity(requestDTO));
        return productoMapper.mapToResponseDTO(accesorio);
    }

    @Override
    @Transactional
    public void deleteProducto(Long id) {
        Producto producto = findProductoActivoById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }


    private Producto createProducto(ProductoRequestDTO dto, Producto entity) {

        if (productoRepository.existsByNombreAndActivoTrue(dto.getNombre())) {
            throw new DataIntegrityViolationException("El nombre de producto '" + dto.getNombre() + "' ya existe.");
        }


        Categoria categoria = categoriaRepository.findByIdAndActivoTrue(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));


        entity.setCategoria(categoria);

        return productoRepository.save(entity);
    }


    private Producto findProductoActivoById(Long id) {
        return productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado o inactivo con ID: " + id));
    }

    @Override
    @Transactional
    public ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO dto) {
        Producto producto = findProductoActivoById(id);


        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStockActual(dto.getStockActual());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setUbicacion(dto.getUbicacion());
        producto.setDisponibleParaVenta(dto.isDisponibleParaVenta());


        if (dto.getCategoriaId() != null) {
            Categoria nuevaCategoria = categoriaRepository.findByIdAndActivoTrue(dto.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
            producto.setCategoria(nuevaCategoria);
        }

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
            throw new IllegalArgumentException("El tipo de producto enviado no coincide con el producto guardado en base de datos.");
        }

        Producto actualizado = productoRepository.save(producto);
        return productoMapper.mapToResponseDTO(actualizado);
    }
}