package co.cue.inventario_service.mapper;


import co.cue.inventario_service.models.dtos.requestdtos.AccesorioRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.AlimentoRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.MedicinaRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.ProductoRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.*;
import co.cue.inventario_service.models.entities.*;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public Alimento mapToEntity(AlimentoRequestDTO dto) {
        Alimento entity = new Alimento();
        mapBaseRequestToEntity(dto, entity);
        entity.setTipoMascota(dto.getTipoMascota());
        entity.setPesoEnKg(dto.getPesoEnKg());
        return entity;
    }

    public Medicina mapToEntity(MedicinaRequestDTO dto) {
        Medicina entity = new Medicina();
        mapBaseRequestToEntity(dto, entity);
        entity.setComposicion(dto.getComposicion());
        entity.setDosisRecomendada(dto.getDosisRecomendada());
        return entity;
    }

    public Accesorio mapToEntity(AccesorioRequestDTO dto) {
        Accesorio entity = new Accesorio();
        mapBaseRequestToEntity(dto, entity);
        entity.setMaterial(dto.getMaterial());
        entity.setTamanio(dto.getTamanio());
        return entity;
    }


    public ProductoResponseDTO mapToResponseDTO(Producto entity) {
        if (entity instanceof Alimento alimento) {
            return mapToResponseDTO(alimento);
        }
        if (entity instanceof Medicina medicina) {
            return mapToResponseDTO(medicina);
        }
        if (entity instanceof Accesorio accesorio) {
            return mapToResponseDTO(accesorio);
        }
        throw new IllegalArgumentException("Tipo de Producto desconocido: " + entity.getClass().getName());
    }


    private CategoriaResponseDTO mapCategoriaDTO(Categoria entity) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    // Mapeador de campos comunes (Request -> Entidad)
    private void mapBaseRequestToEntity(ProductoRequestDTO dto, Producto entity) {
        entity.setNombre(dto.getNombre());
        entity.setPrecio(dto.getPrecio());
        entity.setStockActual(dto.getStockActual());
        entity.setStockMinimo(dto.getStockMinimo());
        entity.setUbicacion(dto.getUbicacion());
        entity.setDisponibleParaVenta(dto.isDisponibleParaVenta());
    }

    // Mapeador de campos comunes (Entidad -> Response)
    private void mapBaseEntityToResponseDTO(Producto entity, ProductoResponseDTO dto) {
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setPrecio(entity.getPrecio());
        dto.setStockActual(entity.getStockActual());
        dto.setDisponibleParaVenta(entity.isDisponibleParaVenta());
        // Mapea la categoría asociada
        if (entity.getCategoria() != null) {
            dto.setCategoria(mapCategoriaDTO(entity.getCategoria()));
        }
    }

    // Mapeadores específicos de Response
    private AlimentoResponseDTO mapToResponseDTO(Alimento entity) {
        AlimentoResponseDTO dto = new AlimentoResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setTipoMascota(entity.getTipoMascota());
        dto.setPesoEnKg(entity.getPesoEnKg());
        return dto;
    }

    private MedicinaResponseDTO mapToResponseDTO(Medicina entity) {
        MedicinaResponseDTO dto = new MedicinaResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setComposicion(entity.getComposicion());
        dto.setDosisRecomendada(entity.getDosisRecomendada());
        return dto;
    }

    private AccesorioResponseDTO mapToResponseDTO(Accesorio entity) {
        AccesorioResponseDTO dto = new AccesorioResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setMaterial(entity.getMaterial());
        dto.setTamanio(entity.getTamanio());
        return dto;
    }
}
