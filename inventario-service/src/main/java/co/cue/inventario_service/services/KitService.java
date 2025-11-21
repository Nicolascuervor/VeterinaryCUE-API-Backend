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

    private final List<IKitMascotaFactory> fabricas;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private Map<String, IKitMascotaFactory> fabricaCache;
    private final CategoriaRepository categoriaRepository;

    @PostConstruct
    public void inicializarCache() {
        fabricaCache = fabricas.stream()
                .collect(Collectors.toMap(IKitMascotaFactory::getTipoMascota, Function.identity()));
    }

    @Transactional
    public ProductoResponseDTO ensamblarKitBienvenida(String tipoMascota) {
        IKitMascotaFactory factory = fabricaCache.get(tipoMascota.toUpperCase());
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de mascota no válido: " + tipoMascota);
        }

        KitMascotaDTO receta = factory.crearKitBienvenida();

        Set<Producto> componentes = new HashSet<>();
        double precioTotalComponentes = 0.0;

        for (KitProductoDTO itemReceta : receta.getComponentes()) {
            Producto componente = productoRepository
                    .findByNombreAndActivoTrue(itemReceta.getNombreProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Componente de kit no encontrado: " + itemReceta.getNombreProducto()));

            if (componente.getStockActual() < itemReceta.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para " + componente.getNombre());
            }

            componentes.add(componente);
            precioTotalComponentes += (componente.getPrecio() * itemReceta.getCantidad());
        }


        Categoria categoriaKits = categoriaRepository.findByNombre("Kits")
                .orElseThrow(() -> new EntityNotFoundException("La categoría 'Kits' no existe. Por favor, créala primero."));


        Producto nuevoKit = new KitProducto();
        nuevoKit.setNombre(receta.getNombreDelKit());
        nuevoKit.setEsKit(true);
        nuevoKit.setActivo(true);
        nuevoKit.setDisponibleParaVenta(true);
        nuevoKit.setStockActual(1);

        double precioFinal = precioTotalComponentes * (1 - receta.getDescuentoPorcentual());
        nuevoKit.setPrecio(precioFinal);

        nuevoKit.setComponentes(componentes);

        nuevoKit.setCategoria(categoriaKits);

        Producto kitGuardado = productoRepository.save(nuevoKit);

        for (Producto componente : componentes) {
            componente.setStockActual(componente.getStockActual() - 1);
            productoRepository.save(componente);
        }

        return productoMapper.mapToResponseDTO(kitGuardado);
    }

}
