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
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/agendamiento/servicios-admin")
@AllArgsConstructor
public class ServicioAdminController {

    private final IServicioAdminService servicioAdminService;


    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> listarTodosLosServicios() {
        return ResponseEntity.ok(servicioAdminService.listarServicios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> obtenerServicioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioAdminService.getServicioById(id));
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


    @PostMapping("/vacunacion")
    public ResponseEntity<ServicioResponseDTO> crearServicioVacunacion(@RequestBody VacunacionRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearVacunacion(dto), HttpStatus.CREATED);
    }


    @PutMapping("/consulta/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioConsulta(
            @PathVariable Long id, @RequestBody ConsultaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarConsulta(id, dto));
    }


    @PutMapping("/cirugia/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioCirugia(
            @PathVariable Long id, @RequestBody CirugiaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarCirugia(id, dto));
    }


    @PutMapping("/estetica/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioEstetica(
            @PathVariable Long id, @RequestBody EsteticaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarEstetica(id, dto));
    }


    @PutMapping("/vacunacion/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicioVacunacion(
            @PathVariable Long id, @RequestBody VacunacionRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarVacunacion(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarServicio(@PathVariable Long id) {
        servicioAdminService.desactivarServicio(id);
        return ResponseEntity.noContent().build();
    }
}
