package co.cue.inventario_service.services;

import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;
import co.cue.inventario_service.models.entities.Categoria;
import co.cue.inventario_service.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaServiceImpl implements ICategoriaService {

    // Repositorio para manejar operaciones de base de datos relacionadas con Categoria
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listAllActiveCategorias() {
        // Obtiene todas las categorías activas y las convierte a DTO
        return categoriaRepository.findAllByActivoTrue() // Usamos el método del Repo
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO findActiveById(Long id) {
        // Busca una categoría activa por ID
        Categoria categoria = findCategoriaActivaById(id);
        return mapToResponseDTO(categoria);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO createCategoria(CategoriaRequestDTO requestDTO) {
        // Valida si ya existe una categoría con ese nombre
        if (categoriaRepository.existsByNombre(requestDTO.getNombre())) {
            throw new DataIntegrityViolationException("El nombre de categoría '" + requestDTO.getNombre() + "' ya existe.");
        }

        // Crea una nueva entidad de categoría
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(requestDTO.getNombre());
        nuevaCategoria.setDescripcion(requestDTO.getDescripcion());

        // Guarda la categoría en la base de datos
        Categoria guardada = categoriaRepository.save(nuevaCategoria);
        return mapToResponseDTO(guardada);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO requestDTO) {

        // Busca la categoría activa a actualizar
        Categoria categoria = findCategoriaActivaById(id);

        // Actualiza los campos
        categoria.setNombre(requestDTO.getNombre());
        categoria.setDescripcion(requestDTO.getDescripcion());

        // Guarda los cambios
        Categoria actualizada = categoriaRepository.save(categoria);
        return mapToResponseDTO(actualizada);
    }

    @Override
    @Transactional
    public void deleteCategoria(Long id) {
        // Busca la categoría activa a eliminar
        Categoria categoria = findCategoriaActivaById(id);

        // Eliminación lógica (cambia activo a false)
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }

    // Método para buscar una categoría activa, lanza excepción si no existe
    private Categoria findCategoriaActivaById(Long id) {
        return categoriaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada o inactiva con ID: " + id));
    }

    // Convierte una entidad Categoria en un DTO de respuesta
    private CategoriaResponseDTO mapToResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
