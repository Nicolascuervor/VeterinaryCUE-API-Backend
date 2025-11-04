package co.cue.inventario_service.services;

import co.cue.inventario_service.models.dtos.requestdtos.CategoriaRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.CategoriaResponseDTO;

import java.util.List;

public interface ICategoriaService {


    List<CategoriaResponseDTO> listAllActiveCategorias();


    CategoriaResponseDTO findActiveById(Long id);


    CategoriaResponseDTO createCategoria(CategoriaRequestDTO requestDTO);

    CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO requestDTO);


    void deleteCategoria(Long id);
}
