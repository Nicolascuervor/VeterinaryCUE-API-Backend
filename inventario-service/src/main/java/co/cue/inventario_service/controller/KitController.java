package co.cue.inventario_service.controller;

import co.cue.inventario_service.models.dtos.responsedtos.ProductoResponseDTO;
import co.cue.inventario_service.services.KitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario/kits")
@AllArgsConstructor
public class KitController {

    private final KitService kitService;


    @PostMapping("/bienvenida/{tipoMascota}")
    public ResponseEntity<ProductoResponseDTO> crearKitBienvenida(
            @PathVariable String tipoMascota) {
        ProductoResponseDTO kitCreado = kitService.ensamblarKitBienvenida(tipoMascota);
        return new ResponseEntity<>(kitCreado, HttpStatus.CREATED);
    }

}
