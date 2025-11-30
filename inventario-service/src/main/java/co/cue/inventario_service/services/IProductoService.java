package co.cue.inventario_service.services;


import co.cue.inventario_service.models.dtos.requestdtos.*;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;

import java.util.List;

public interface IProductoService {

    // Retorna todos los productos que están activos
    List<ProductoResponseDTO> listAllActiveProductos();

    // Busca un producto activo por su ID
    ProductoResponseDTO findActiveById(Long id);

    // Crea un producto de tipo Alimento
    ProductoResponseDTO createAlimento(AlimentoRequestDTO requestDTO);

    // Crea un producto de tipo Medicina
    ProductoResponseDTO createMedicina(MedicinaRequestDTO requestDTO);

    // Crea un producto de tipo Accesorio
    ProductoResponseDTO createAccesorio(AccesorioRequestDTO requestDTO);

    // Elimina un producto marcándolo como inactivo (eliminación lógica)
    void deleteProducto(Long id);

    // Actualiza la información general de un producto
    ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO dto);

    // Reduce el stock de una lista de productos según las cantidades indicadas
    void descontarStock(List<StockReductionDTO> items);

}
