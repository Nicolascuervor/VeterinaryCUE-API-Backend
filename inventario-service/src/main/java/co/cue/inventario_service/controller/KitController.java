package co.cue.inventario_service.controller;

import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.services.KitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Expone endpoints REST para los kits
@RequestMapping("/api/inventario/kits") // Ruta base para las operaciones de kits
@AllArgsConstructor // Genera un constructor con los atributos final
public class KitController {

    // Servicio encargado de ensamblar kits de bienvenida
    private final KitService kitService;

    // POST: Crea un kit de bienvenida según el tipo de mascota solicitado
    @PostMapping("/bienvenida/{tipoMascota}")
    public ResponseEntity<ProductoResponseDTO> crearKitBienvenida(
            @PathVariable String tipoMascota) {
        ProductoResponseDTO kitCreado = kitService.ensamblarKitBienvenida(tipoMascota);
        return new ResponseEntity<>(kitCreado, HttpStatus.CREATED);
    }

}
