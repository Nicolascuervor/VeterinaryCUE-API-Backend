package co.cue.inventario_service.services;

import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.BulkCategoriaResponseDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;
import co.cue.inventario_service.models.entities.Categoria;
import co.cue.inventario_service.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
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

    @Override
    public BulkCategoriaResponseDTO crearCategoriasMasivo(List<CategoriaRequestDTO> categorias) {
        log.info("Iniciando creación masiva de {} categorías", categorias.size());
        
        List<CategoriaResponseDTO> categoriasCreadas = new ArrayList<>();
        List<BulkCategoriaResponseDTO.ErrorCategoriaDTO> errores = new ArrayList<>();
        
        for (int i = 0; i < categorias.size(); i++) {
            CategoriaRequestDTO categoriaDTO = categorias.get(i);
            String nombreCategoria = "Sin nombre";
            
            try {
                // Validar que el DTO no sea null
                if (categoriaDTO == null) {
                    throw new IllegalArgumentException("La categoría en el índice " + i + " es null");
                }
                
                nombreCategoria = categoriaDTO.getNombre() != null ? categoriaDTO.getNombre() : "Sin nombre";
                
                // Validar campos requeridos
                validarCategoriaDTO(categoriaDTO, i);
                
                // Crear categoría en su propia transacción
                CategoriaResponseDTO categoriaCreada = crearCategoriaTransaccional(categoriaDTO);
                categoriasCreadas.add(categoriaCreada);
                log.debug("Categoría {} (índice {}) creada exitosamente: {}", nombreCategoria, i, categoriaCreada.getId());
            } catch (Exception e) {
                String mensajeError = extraerMensajeError(e);
                errores.add(new BulkCategoriaResponseDTO.ErrorCategoriaDTO(i, nombreCategoria, mensajeError));
                log.error("Error al crear categoría {} (índice {}): {}", nombreCategoria, i, mensajeError, e);
            }
        }
        
        int totalProcesadas = categorias.size();
        int totalExitosas = categoriasCreadas.size();
        int totalFallidas = errores.size();
        
        log.info("Creación masiva completada: {} procesadas, {} exitosas, {} fallidas", 
                totalProcesadas, totalExitosas, totalFallidas);
        
        return new BulkCategoriaResponseDTO(categoriasCreadas, errores, totalProcesadas, totalExitosas, totalFallidas);
    }
    
    /**
     * Valida que el DTO tenga los campos requeridos.
     */
    private void validarCategoriaDTO(CategoriaRequestDTO dto, int indice) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es requerido (índice " + indice + ")");
        }
    }
    
    /**
     * Crea una categoría en una transacción separada.
     * Esto permite que cada categoría se cree independientemente.
     */
    @Transactional(rollbackFor = Exception.class)
    public CategoriaResponseDTO crearCategoriaTransaccional(CategoriaRequestDTO dto) {
        return createCategoria(dto);
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

    // Convierte una entidad Categoria en un DTO de respuesta
    private CategoriaResponseDTO mapToResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
