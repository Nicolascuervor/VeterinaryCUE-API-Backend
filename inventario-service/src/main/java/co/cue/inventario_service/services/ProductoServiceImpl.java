package co.cue.inventario_service.services;

import co.cue.inventario_service.mapper.ProductoMapper;
import co.cue.inventario_service.models.dtos.*;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor // (Mentor): Inyección por constructor
public class ProductoServiceImpl implements IProductoService {

    // (Mentor): Inyectamos todos los componentes que necesitamos.
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listAllActiveProductos() {
        return productoRepository.findAllByActivoTrue()
                .stream()
                // (Mentor): Usamos el mapper para convertir cada entidad al DTO polimórfico
                .map(productoMapper::mapToResponseDTO)
                .collect(Collectors.toList());
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
        // (Mentor): Reutilizamos la lógica de validación y creación
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
}