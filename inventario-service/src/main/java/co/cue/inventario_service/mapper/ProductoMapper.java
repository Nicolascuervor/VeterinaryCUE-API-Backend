package co.cue.inventario_service.mapper;


import co.cue.inventario_service.models.dtos.requestdtos.AccesorioRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.AlimentoRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.MedicinaRequestDTO;
import co.cue.inventario_service.models.dtos.requestdtos.ProductoRequestDTO;
import co.cue.inventario_service.models.dtos.responsedtos.*;
import co.cue.inventario_service.models.entities.*;
import org.springframework.stereotype.Component;
/**
 * Componente encargado de la transformación de datos (Mapping) entre DTOs y Entidades de Producto.
 * Esta clase aísla la lógica de conversión, permitiendo que los Servicios se enfoquen
 * en la lógica de negocio y los Controladores en la comunicación HTTP.
 * Su responsabilidad principal es manejar el polimorfismo del inventario:
 * Sabe cómo convertir solicitudes específicas (ej. crear un Alimento) en la entidad correcta
 * y cómo transformar una entidad genérica Producto recuperada de la BD en su DTO específico
 * correspondiente para la respuesta.
 */
@Component
public class ProductoMapper {

    /**
     * Convierte un DTO de solicitud de Alimento en su entidad correspondiente.
     * Inicializa una entidad Alimento vacía, llena los campos base (nombre, precio, stock)
     * utilizando el método compartido y luego asigna los atributos específicos
     * de este tipo de producto (tipo mascota, peso).
     */
    public Alimento mapToEntity(AlimentoRequestDTO dto) {
        Alimento entity = new Alimento();
        mapBaseRequestToEntity(dto, entity);
        entity.setTipoMascota(dto.getTipoMascota());
        entity.setPesoEnKg(dto.getPesoEnKg());
        return entity;
    }

    /**
     * Convierte un DTO de solicitud de Medicina en su entidad correspondiente.
     * Similar al anterior, pero asigna los campos específicos de medicina
     * (composición, dosis recomendada).
     */
    public Medicina mapToEntity(MedicinaRequestDTO dto) {
        Medicina entity = new Medicina();
        mapBaseRequestToEntity(dto, entity);
        entity.setComposicion(dto.getComposicion());
        entity.setDosisRecomendada(dto.getDosisRecomendada());
        return entity;
    }

    /**
     * Convierte un DTO de solicitud de Accesorio en su entidad correspondiente.
     * Asigna campos específicos como material y tamaño.
     */
    public Accesorio mapToEntity(AccesorioRequestDTO dto) {
        Accesorio entity = new Accesorio();
        mapBaseRequestToEntity(dto, entity);
        entity.setMaterial(dto.getMaterial());
        entity.setTamanio(dto.getTamanio());
        return entity;
    }

    /**
     * Método polimórfico para convertir cualquier Entidad Producto a su DTO de respuesta.
     * Este método es crítico para las consultas generales (listar todo el inventario).
     * Inspecciona el tipo real de la instancia (usando instanceof) y delega la conversión
     * al método especializado correspondiente. Esto asegura que el cliente reciba
     * el objeto JSON con todos los campos específicos, no solo los genéricos.
     */
    public ProductoResponseDTO mapToResponseDTO(Producto entity) {
        // Java 16+ Pattern Matching for instanceof simplificaría esto,
        // pero mantenemos la estructura clásica para claridad educativa.
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

    /**
     * Convierte una entidad Categoria a su DTO simple.
     * Se usa para anidar la información de la categoría dentro de la respuesta del producto,
     * evitando ciclos infinitos o exponer demasiada información de la relación.
     */
    private CategoriaResponseDTO mapCategoriaDTO(Categoria entity) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    /**
     * Método auxiliar para mapear los atributos comunes desde el Request hacia la Entidad.
     * Aplica el principio DRY (Don't Repeat Yourself). Todos los productos, sin importar
     * su tipo, comparten estos campos (nombre, precio, stock, ubicación).
     */
    private void mapBaseRequestToEntity(ProductoRequestDTO dto, Producto entity) {
        entity.setNombre(dto.getNombre());
        entity.setPrecio(dto.getPrecio());
        entity.setStockActual(dto.getStockActual());
        entity.setStockMinimo(dto.getStockMinimo());
        entity.setUbicacion(dto.getUbicacion());
        entity.setDisponibleParaVenta(dto.isDisponibleParaVenta());
    }

    /**
     * Método auxiliar para mapear los atributos comunes desde la Entidad hacia el Response.
     * Transfiere los datos base y resuelve la relación con la Categoría si existe.
     */
    private void mapBaseEntityToResponseDTO(Producto entity, ProductoResponseDTO dto) {
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setPrecio(entity.getPrecio());
        dto.setStockActual(entity.getStockActual());
        dto.setDisponibleParaVenta(entity.isDisponibleParaVenta());
        if (entity.getCategoria() != null) {
            dto.setCategoria(mapCategoriaDTO(entity.getCategoria()));
        }
    }

    // --- Métodos Privados de Mapeo Específico ---
    // Estos métodos completan el mapeo llenando los campos exclusivos de cada subclase.

    private AlimentoResponseDTO mapAlimentoToResponseDTO(Alimento entity) {
        AlimentoResponseDTO dto = new AlimentoResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setTipoMascota(entity.getTipoMascota());
        dto.setPesoEnKg(entity.getPesoEnKg());
        return dto;
    }

    private MedicinaResponseDTO mapMedicinaToResponseDTO(Medicina entity) {
        MedicinaResponseDTO dto = new MedicinaResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setComposicion(entity.getComposicion());
        dto.setDosisRecomendada(entity.getDosisRecomendada());
        return dto;
    }

    private AccesorioResponseDTO mapAccesorioToResponseDTO(Accesorio entity) {
        AccesorioResponseDTO dto = new AccesorioResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        dto.setMaterial(entity.getMaterial());
        dto.setTamanio(entity.getTamanio());
        return dto;
    }

    private KitProductoResponseDTO mapKitToResponseDTO(KitProducto entity) {
        KitProductoResponseDTO dto = new KitProductoResponseDTO();
        mapBaseEntityToResponseDTO(entity, dto);
        // (Nota): Los kits podrían requerir mapear sus componentes internos aquí si se desea.
        return dto;
    }
}
