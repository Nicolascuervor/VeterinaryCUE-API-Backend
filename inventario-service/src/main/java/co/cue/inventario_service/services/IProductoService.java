package co.cue.inventario_service.services;


import co.cue.inventario_service.models.dtos.requestdtos.AccesorioRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.AlimentoRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.MedicinaRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.ProductoRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;

import java.util.List;

public interface IProductoService {

    List<ProductoResponseDTO> listAllActiveProductos();


    ProductoResponseDTO findActiveById(Long id);


    ProductoResponseDTO createAlimento(AlimentoRequestDTO requestDTO);

    ProductoResponseDTO createMedicina(MedicinaRequestDTO requestDTO);

    ProductoResponseDTO createAccesorio(AccesorioRequestDTO requestDTO);

    void deleteProducto(Long id);

    ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO dto);

}
