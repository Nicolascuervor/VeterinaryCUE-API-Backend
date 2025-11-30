package co.cue.inventario_service.services;

import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;

import java.util.List;

public interface ICategoriaService {

    // Retorna una lista de todas las categorías que están activas
    List<CategoriaResponseDTO> listAllActiveCategorias();

    // Busca una categoría activa por su ID y la retorna en un DTO
    CategoriaResponseDTO findActiveById(Long id);

    // Crea una nueva categoría usando los datos enviados en el request DTO
    CategoriaResponseDTO createCategoria(CategoriaRequestDTO requestDTO);

    // Actualiza la información de una categoría según su ID
    CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO requestDTO);

    // Realiza una eliminación lógica marcando la categoría como inactiva
    void deleteCategoria(Long id);
}
