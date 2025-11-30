package co.cue.inventario_service.services;


import co.cue.inventario_service.mapper.ProductoMapper;
import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.models.entities.Categoria;
import co.cue.inventario_service.models.entities.KitProducto;
import co.cue.inventario_service.models.entities.Producto;
import co.cue.inventario_service.patterns.IKitMascotaFactory;
import co.cue.inventario_service.patterns.kitfactory.KitMascotaDTO;
import co.cue.inventario_service.patterns.kitfactory.KitProductoDTO;
import co.cue.inventario_service.repository.CategoriaRepository;
import co.cue.inventario_service.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KitService {

    // Lista de todas las fábricas registradas (perro, gato, etc.)
    private final List<IKitMascotaFactory> fabricas;

    // Repositorio para buscar productos
    private final ProductoRepository productoRepository;

    // Mapper para convertir entidad → DTO
    private final ProductoMapper productoMapper;

    // Cache para acceder a las fábricas por tipo de mascota
    private Map<String, IKitMascotaFactory> fabricaCache;

    // Repositorio de categorías
    private final CategoriaRepository categoriaRepository;

    @PostConstruct
    public void inicializarCache() {
        // Carga las fábricas en un mapa para acceso rápido por clave
        fabricaCache = fabricas.stream()
                .collect(Collectors.toMap(IKitMascotaFactory::getTipoMascota, Function.identity()));
    }

    @Transactional
    public ProductoResponseDTO ensamblarKitBienvenida(String tipoMascota) {
        // Se obtiene la fábrica según el tipo de mascota
        IKitMascotaFactory factory = fabricaCache.get(tipoMascota.toUpperCase());
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de mascota no válido: " + tipoMascota);
        }

        // Se obtiene la receta del kit (componentes + descuento + nombre del kit)
        KitMascotaDTO receta = factory.crearKitBienvenida();

        // Lista para guardar los productos reales que componen el kit
        Set<Producto> componentes = new HashSet<>();
        double precioTotalComponentes = 0.0;

        // Validación y agregación de cada componente
        for (KitProductoDTO itemReceta : receta.getComponentes()) {

            // Buscar producto por nombre
            Producto componente = productoRepository
                    .findByNombreAndActivoTrue(itemReceta.getNombreProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Componente de kit no encontrado: " + itemReceta.getNombreProducto()));
            // Verificar que el stock sea suficiente
            if (componente.getStockActual() < itemReceta.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para " + componente.getNombre());
            }
            // Agregar producto al conjunto
            componentes.add(componente);

            // Sumar el precio multiplicado por la cantidad requerida
            precioTotalComponentes += (componente.getPrecio() * itemReceta.getCantidad());
        }

        // Buscar categoría "Kits"
        Categoria categoriaKits = categoriaRepository.findByNombre("Kits")
                .orElseThrow(() -> new EntityNotFoundException("La categoría 'Kits' no existe. Por favor, créala primero."));

        // Construcción del nuevo kit como producto
        Producto nuevoKit = new KitProducto();
        nuevoKit.setNombre(receta.getNombreDelKit());
        nuevoKit.setEsKit(true);// Marcamos que es un kit
        nuevoKit.setActivo(true);
        nuevoKit.setDisponibleParaVenta(true);
        nuevoKit.setStockActual(1);// Solo se genera un kit


        // Precio final aplicando descuento
        double precioFinal = precioTotalComponentes * (1 - receta.getDescuentoPorcentual());
        nuevoKit.setPrecio(precioFinal);

        // Se asignan los componentes al kit
        nuevoKit.setComponentes(componentes);

        // Asignar categoría
        nuevoKit.setCategoria(categoriaKits);

        // Guardar el kit en la base de datos
        Producto kitGuardado = productoRepository.save(nuevoKit);

        // Reducir stock de cada componente
        for (Producto componente : componentes) {
            componente.setStockActual(componente.getStockActual() - 1);
            productoRepository.save(componente);
        }

        // Retornar DTO del kit
        return productoMapper.mapToResponseDTO(kitGuardado);
    }

}
