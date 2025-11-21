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

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listAllActiveCategorias() {
        return categoriaRepository.findAllByActivoTrue() // Usamos el método del Repo
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO findActiveById(Long id) {
        Categoria categoria = findCategoriaActivaById(id);
        return mapToResponseDTO(categoria);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO createCategoria(CategoriaRequestDTO requestDTO) {
        if (categoriaRepository.existsByNombre(requestDTO.getNombre())) {
            throw new DataIntegrityViolationException("El nombre de categoría '" + requestDTO.getNombre() + "' ya existe.");
        }

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(requestDTO.getNombre());
        nuevaCategoria.setDescripcion(requestDTO.getDescripcion());

        Categoria guardada = categoriaRepository.save(nuevaCategoria);
        return mapToResponseDTO(guardada);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO requestDTO) {
        Categoria categoria = findCategoriaActivaById(id);

        categoria.setNombre(requestDTO.getNombre());
        categoria.setDescripcion(requestDTO.getDescripcion());

        Categoria actualizada = categoriaRepository.save(categoria);
        return mapToResponseDTO(actualizada);
    }

    @Override
    @Transactional
    public void deleteCategoria(Long id) {
        Categoria categoria = findCategoriaActivaById(id);
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }

    private Categoria findCategoriaActivaById(Long id) {
        return categoriaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada o inactiva con ID: " + id));
    }

    private CategoriaResponseDTO mapToResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
