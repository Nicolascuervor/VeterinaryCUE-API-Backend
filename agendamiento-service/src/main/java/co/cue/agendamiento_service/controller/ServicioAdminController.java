package co.cue.agendamiento_service.controller;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;


import co.cue.agendamiento_service.models.dtos.response.ServicioResponseDTO;
import co.cue.agendamiento_service.service.IServicioAdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Arquitecto): Este controlador sigue el patrón que ya usaron en ProductoController.java.
 * Su única responsabilidad es el CRUD de los Tipos de Servicio.
 *
 * (Consultor): Note el @PreAuthorize. La seguridad a nivel de microservicio es
 * vital. Asumimos que (como en sus otros servicios) tenemos Spring Security
 * configurado para leer roles del JWT.
 */
@RestController
@RequestMapping("/api/agendamiento/servicios-admin")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ServicioAdminController {

    private final IServicioAdminService servicioAdminService;

    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> listarTodosLosServicios() {
        return ResponseEntity.ok(servicioAdminService.listarServicios());
    }

    @PostMapping("/consulta")
    public ResponseEntity<ServicioResponseDTO> crearServicioConsulta(@RequestBody ConsultaRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearConsulta(dto), HttpStatus.CREATED);
    }

    @PostMapping("/cirugia")
    public ResponseEntity<ServicioResponseDTO> crearServicioCirugia(@RequestBody CirugiaRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearCirugia(dto), HttpStatus.CREATED);
    }

    @PostMapping("/estetica")
    public ResponseEntity<ServicioResponseDTO> crearServicioEstetica(@RequestBody EsteticaRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearEstetica(dto), HttpStatus.CREATED);
    }

    // (Aquí irían los @PutMapping y @DeleteMapping para actualizar/desactivar servicios)
}
