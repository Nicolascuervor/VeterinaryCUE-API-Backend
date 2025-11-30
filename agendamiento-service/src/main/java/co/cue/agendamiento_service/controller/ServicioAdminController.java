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

// Define esta clase como un controlador REST que responderá solicitudes HTTP.
@RestController

// Establece la ruta base para todos los endpoints de este controlador.
@RequestMapping("/api/agendamiento/servicios-admin")

// Genera automáticamente un constructor con todas las dependencias inyectadas.
@AllArgsConstructor
public class ServicioAdminController {

    private final IServicioAdminService servicioAdminService; // Servicio que contiene la lógica de negocio de los servicios administrativos.


    @GetMapping  // Endpoint GET en la ruta base para obtener todos los servicios.
    public ResponseEntity<List<ServicioResponseDTO>> listarTodosLosServicios() {

        // Llama al servicio para listar todos los servicios y devuelve la respuesta HTTP 200 con la lista.
        return ResponseEntity.ok(servicioAdminService.listarServicios());
    }


    @GetMapping("/{id}") // Endpoint GET para obtener un servicio por su ID.
    public ResponseEntity<ServicioResponseDTO> obtenerServicioPorId(@PathVariable Long id) {

        // Llama al servicio para obtener un servicio específico por ID y devuelve HTTP 200.
        return ResponseEntity.ok(servicioAdminService.getServicioById(id));
    }


    @PostMapping("/consulta") // Endpoint POST para crear un servicio de consulta.
    public ResponseEntity<ServicioResponseDTO> crearServicioConsulta(@RequestBody ConsultaRequestDTO dto) {

        // Llama al servicio para crear la consulta y devuelve HTTP 201 Created
        return new ResponseEntity<>(servicioAdminService.crearConsulta(dto), HttpStatus.CREATED);
    }

    @PostMapping("/cirugia")  // Endpoint POST para crear un servicio de cirugía.
    public ResponseEntity<ServicioResponseDTO> crearServicioCirugia(@RequestBody CirugiaRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearCirugia(dto), HttpStatus.CREATED);
    }

    @PostMapping("/estetica")  // Endpoint POST para crear un servicio de estética.
    public ResponseEntity<ServicioResponseDTO> crearServicioEstetica(@RequestBody EsteticaRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearEstetica(dto), HttpStatus.CREATED);
    }


    @PostMapping("/vacunacion") // Endpoint POST para crear un servicio de vacunación.
    public ResponseEntity<ServicioResponseDTO> crearServicioVacunacion(@RequestBody VacunacionRequestDTO dto) {
        return new ResponseEntity<>(servicioAdminService.crearVacunacion(dto), HttpStatus.CREATED);
    }


    @PutMapping("/consulta/{id}") // Endpoint PUT para actualizar un servicio de consulta por ID.
    public ResponseEntity<ServicioResponseDTO> actualizarServicioConsulta(
            @PathVariable Long id, @RequestBody ConsultaRequestDTO dto) {

        // Llama al servicio para actualizar la consulta y devuelve HTTP 200.
        return ResponseEntity.ok(servicioAdminService.actualizarConsulta(id, dto));
    }


    @PutMapping("/cirugia/{id}") // Endpoint PUT para actualizar un servicio de cirugía por ID.
    public ResponseEntity<ServicioResponseDTO> actualizarServicioCirugia(
            @PathVariable Long id, @RequestBody CirugiaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarCirugia(id, dto));
    }


    @PutMapping("/estetica/{id}")  // Endpoint PUT para actualizar un servicio de estética por ID.
    public ResponseEntity<ServicioResponseDTO> actualizarServicioEstetica(
            @PathVariable Long id, @RequestBody EsteticaRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarEstetica(id, dto));
    }


    @PutMapping("/vacunacion/{id}") // Endpoint PUT para actualizar un servicio de vacunación por ID.
    public ResponseEntity<ServicioResponseDTO> actualizarServicioVacunacion(
            @PathVariable Long id, @RequestBody VacunacionRequestDTO dto) {
        return ResponseEntity.ok(servicioAdminService.actualizarVacunacion(id, dto));
    }


    @DeleteMapping("/{id}")  // Endpoint DELETE para desactivar un servicio por su ID.
    public ResponseEntity<Void> desactivarServicio(@PathVariable Long id) {
        servicioAdminService.desactivarServicio(id);
        return ResponseEntity.noContent().build();
    }
}
