package co.cue.agendamiento_service.controller;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;


import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.ServicioResponseDTO;
import co.cue.agendamiento_service.services.IServicioAdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/agendamiento/servicios-admin")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de clase
public class ServicioAdminController {

    private final IServicioAdminService servicioAdminService;

    // --- Endpoints GET ---

    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> listarTodosLosServicios() {
        return ResponseEntity.ok(servicioAdminService.listarServicios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> obtenerServicioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioAdminService.getServicioById(id));
    }

    // --- Endpoints POST (Creación) ---

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

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @PostMapping("/vacunacion")
    public ResponseEntity<ServicioResponseDTO> crearServicioVacunacion(@RequestBody VacunacionRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearVacunacion(dto), HttpStatus.CREATED);
    }

    // --- Endpoints PUT (Actualización) ---

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @PutMapping("/consulta/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioConsulta(
            @PathVariable Long id, @RequestBody ConsultaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarConsulta(id, dto));
    }

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @PutMapping("/cirugia/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioCirugia(
            @PathVariable Long id, @RequestBody CirugiaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarCirugia(id, dto));
    }

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @PutMapping("/estetica/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioEstetica(
            @PathVariable Long id, @RequestBody EsteticaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarEstetica(id, dto));
    }

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @PutMapping("/vacunacion/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioVacunacion(
            @PathVariable Long id, @RequestBody VacunacionRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarVacunacion(id, dto));
    }


    // --- Endpoint DELETE (Desactivación) ---

    /**
     * (Implementación Faltante - AÑADIDA)
     * (Mentor): Usamos @DeleteMapping para el "Soft Delete".
     * Devuelve 204 No Content, que es la semántica correcta para
     * una operación DELETE exitosa que no devuelve cuerpo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarServicio(@PathVariable Long id) {
        servicioAdminService.desactivarServicio(id);
        return ResponseEntity.noContent().build();
    }
}
