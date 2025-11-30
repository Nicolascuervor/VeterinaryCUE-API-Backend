package co.cue.inventario_service.controller;

import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.services.KitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST especializado en la gestión de productos tipo 'Kit'.
 * Los Kits son productos compuestos (Composite Pattern) que agrupan varios items
 * individuales (alimentos, juguetes, etc.) bajo un solo precio y SKU.
 * Este controlador expone operaciones de alto nivel para el "ensamblaje" automático
 * de estos paquetes, basándose en reglas de negocio predefinidas (Factories).
 * Política de Acceso:
 * - Las operaciones de creación (POST) están restringidas a Administradores,
 * ya que implican la creación de nuevos productos en el catálogo y el descuento
 * de stock de los componentes base.
 */
@RestController
@RequestMapping("/api/inventario/kits")
@AllArgsConstructor
public class KitController {

    private final KitService kitService;

    /**
     * Genera y registra un nuevo "Kit de Bienvenida" predefinido en el sistema.
     * A diferencia de crear un producto normal donde se envían todos los datos,
     * aquí solo indicamos el "tipo de mascota". El sistema actúa como una línea de montaje:
     * 1. Selecciona la "receta" (Abstract Factory) adecuada (Perro o Gato).
     * 2. Verifica que existan los componentes en inventario.
     * 3. Crea el nuevo producto "Kit" y descuenta el stock de los componentes individuales.
     */
    @PostMapping("/bienvenida/{tipoMascota}")
    public ResponseEntity<ProductoResponseDTO> crearKitBienvenida(
            @PathVariable String tipoMascota) {
        ProductoResponseDTO kitCreado = kitService.ensamblarKitBienvenida(tipoMascota);
        return new ResponseEntity<>(kitCreado, HttpStatus.CREATED);
    }

}