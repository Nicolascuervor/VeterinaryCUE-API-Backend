package co.cue.inventario_service.mapper;


import co.cue.inventario_service.models.dtos.requestdtos.AccesorioRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.AlimentoRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.MedicinaRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.ProductoRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.*;
import co.cue.inventario_service.models.entities.*;
import org.springframework.stereotype.Component;

@Component // Componente encargado de convertir entre entidades y DTOs
public class ProductoMapper {

    // Convierte un DTO de Alimento a su entidad correspondiente
    public Alimento mapToEntity(AlimentoRequestDTO dto) {
        Alimento entity = new Alimento();
        mapBaseRequestToEntity(dto, entity);
        entity.setTipoMascota(dto.getTipoMascota());
        entity.setPesoEnKg(dto.getPesoEnKg());
        return entity;
    }

    // Convierte un DTO de Medicina a su entidad correspondiente
    public Medicina mapToEntity(MedicinaRequestDTO dto) {
        Medicina entity = new Medicina();
        mapBaseRequestToEntity(dto, entity);
        entity.setComposicion(dto.getComposicion());
        entity.setDosisRecomendada(dto.getDosisRecomendada());
        return entity;
    }

    // Convierte un DTO de Accesorio a su entidad correspondiente
    public Accesorio mapToEntity(AccesorioRequestDTO dto) {
        Accesorio entity = new Accesorio();
        mapBaseRequestToEntity(dto, entity);
        entity.setMaterial(dto.getMaterial());
        entity.setTamanio(dto.getTamanio());
        return entity;
    }

    // Convierte una entidad de Producto (según su tipo) al DTO de respuesta adecuado
    public ProductoResponseDTO mapToResponseDTO(Producto entity) {
        if (entity instanceof Alimento alimento) {
            return mapAlimentoToResponseDTO(alimento);
        }
        if (entity instanceof Medicina medicina) {
            return mapMedicinaToResponseDTO(medicina);
        }
        if (entity instanceof Accesorio accesorio) {
            return mapAccesorioToResponseDTO(accesorio);
        }
        if (entity instanceof KitProducto kit) {
            return mapKitToResponseDTO(kit);
        }
        throw new IllegalArgumentException("Tipo de Producto desconocido: " + entity.getClass().getName());
    }

    // Convierte una entidad de Categoría a su DTO correspondiente
    private CategoriaResponseDTO mapCategoriaDTO(Categoria entity) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    // Mapea atributos comunes de Producto desde el DTO hacia la entidad
    private void mapBaseRequestToEntity(ProductoRequestDTO dto, Producto entity) {
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFoto(dto.getFoto());
        entity.setPrecio(dto.getPrecio());
        entity.setStockActual(dto.getStockActual());
        entity.setStockMinimo(dto.getStockMinimo());
        entity.setUbicacion(dto.getUbicacion());
        entity.setDisponibleParaVenta(dto.isDisponibleParaVenta());
    }

    // Mapea atributos comunes desde la entidad hacia el DTO base
    private void mapBaseEntityToResponseDTO(Producto entity, ProductoResponseDTO dto) {
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFoto(entity.getFoto());
        dto.setPrecio(entity.getPrecio());
        dto.setStockActual(entity.getStockActual());
        dto.setDisponibleParaVenta(entity.isDisponibleParaVenta());
        if (entity.getCategoria() != null) {
            dto.setCategoria(mapCategoriaDTO(entity.getCategoria()));
        }
    }
    // Convierte una entidad Alimento a su DTO correspondiente
    private AlimentoResponseDTO mapAlimentoToResponseDTO(Alimento entity) {
        AlimentoResponseDTO dto = new AlimentoResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setTipoMascota(entity.getTipoMascota());
        dto.setPesoEnKg(entity.getPesoEnKg());
        return dto;
    }
    // Convierte una entidad Medicina a su DTO correspondiente
    private MedicinaResponseDTO mapMedicinaToResponseDTO(Medicina entity) {
        MedicinaResponseDTO dto = new MedicinaResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setComposicion(entity.getComposicion());
        dto.setDosisRecomendada(entity.getDosisRecomendada());
        return dto;
    }
    // Convierte una entidad Accesorio a su DTO correspondiente
    private AccesorioResponseDTO mapAccesorioToResponseDTO(Accesorio entity) {
        AccesorioResponseDTO dto = new AccesorioResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setMaterial(entity.getMaterial());
        dto.setTamanio(entity.getTamanio());
        return dto;
    }
    // Convierte una entidad de KitProducto a su DTO correspondiente
    private KitProductoResponseDTO mapKitToResponseDTO(KitProducto entity) {
        KitProductoResponseDTO dto = new KitProductoResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        return dto;
}
}
