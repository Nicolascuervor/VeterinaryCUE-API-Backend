package co.cue.agendamiento_service.controller;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ServicioRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.BulkServicioResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.ServicioResponseDTO;
import co.cue.agendamiento_service.services.IServicioAdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

// Define esta clase como un controlador REST que responderá solicitudes HTTP.
@RestController

// Establece la ruta base para todos los endpoints de este controlador.
@RequestMapping("/api/agendamiento/servicios-admin")

// Genera automáticamente un constructor con todas las dependencias inyectadas.
@AllArgsConstructor
@Slf4j
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

    /**
     * Endpoint POST para crear múltiples servicios de forma masiva.
     * Acepta una lista de servicios de cualquier tipo (Consulta, Cirugía, Estética, Vacunación).
     * Cada servicio debe incluir el campo "tipoServicio" para identificar su tipo.
     * 
     * El endpoint procesa cada servicio individualmente, permitiendo que algunos se creen
     * exitosamente aunque otros fallen. Devuelve un resumen con los servicios creados y los errores.
     * 
     * @param servicios Lista de servicios a crear (pueden ser de diferentes tipos)
     * @return ResponseEntity con el resultado de la operación masiva
     */
    @PostMapping("/bulk")  // Endpoint POST para creación masiva de servicios
    public ResponseEntity<BulkServicioResponseDTO> crearServiciosMasivo(
            @RequestBody List<ServicioRequestDTO> servicios) {
        
        try {
            if (servicios == null || servicios.isEmpty()) {
                log.warn("Intento de crear servicios masivos con lista null o vacía");
                return ResponseEntity.badRequest().build();
            }
            
            log.info("Recibida solicitud para crear {} servicios masivamente", servicios.size());
            BulkServicioResponseDTO resultado = servicioAdminService.crearServiciosMasivo(servicios);
            
            // Si todos los servicios fallaron, devolver 400 Bad Request
            // Si algunos fallaron pero otros tuvieron éxito, devolver 207 Multi-Status (pero Spring no lo soporta nativamente)
            // Por simplicidad, devolvemos 201 si hay al menos un éxito, o 400 si todos fallaron
            if (resultado.getTotalExitosos() > 0) {
                log.info("Creación masiva completada: {} exitosos de {} procesados", 
                        resultado.getTotalExitosos(), resultado.getTotalProcesados());
                return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
            } else {
                log.warn("Todos los servicios fallaron en la creación masiva");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
            }
        } catch (HttpMessageNotReadableException e) {
            log.error("Error de deserialización JSON: {}", e.getMessage(), e);
            BulkServicioResponseDTO errorResponse = crearErrorResponse(
                "Error al deserializar el JSON. Verifique que cada servicio tenga el campo 'tipoServicio' y que sea válido (CONSULTA, CIRUGIA, ESTETICA, VACUNACION). " + 
                "Detalle: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            log.error("Error inesperado al procesar creación masiva: {}", e.getMessage(), e);
            BulkServicioResponseDTO errorResponse = crearErrorResponse(
                "Error inesperado al procesar la solicitud: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Crea una respuesta de error estándar.
     */
    private BulkServicioResponseDTO crearErrorResponse(String mensajeError) {
        BulkServicioResponseDTO errorResponse = new BulkServicioResponseDTO();
        errorResponse.setServiciosCreados(new ArrayList<>());
        errorResponse.setErrores(new ArrayList<>());
        errorResponse.getErrores().add(new BulkServicioResponseDTO.ErrorServicioDTO(
            0, "Error general", "ERROR", mensajeError
        ));
        errorResponse.setTotalProcesados(0);
        errorResponse.setTotalExitosos(0);
        errorResponse.setTotalFallidos(1);
        return errorResponse;
    }
}
