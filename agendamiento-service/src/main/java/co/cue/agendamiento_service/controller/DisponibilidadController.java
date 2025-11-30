package co.cue.agendamiento_service.controller;

import co.cue.agendamiento_service.models.entities.dtos.DisponibilidadResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.JornadaLaboralResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import co.cue.agendamiento_service.services.IAgendamientoService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
// Indica que esta clase es un controlador REST que manejará solicitudes HTTP.
@RestController

// Define la ruta base para todos los endpoints de este controlador.
@RequestMapping("/api/agendamiento/disponibilidad")

// Genera un constructor con todas las dependencias requeridas.
@AllArgsConstructor

public class DisponibilidadController {

    // Servicio que contiene la lógica del agendamiento.
    private final IAgendamientoService agendamientoService;

    // Expone un endpoint POST en /jornada.
    @PostMapping("/jornada")

    // Solo ADMIN o VETERINARIO pueden acceder.
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<JornadaLaboralResponseDTO> crearActualizarJornada(@RequestBody JornadaLaboralRequestDTO dto) {

        // Llama al servicio para crear o actualizar la jornada laboral usando los datos recibidos.
        return ResponseEntity.ok(agendamientoService.crearActualizarJornada(dto));
    }

    // Expone un endpoint POST para generar slots de disponibilidad.
    @PostMapping("/generar-slots")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede generar slots.
    public ResponseEntity<Void> generarSlots(
            @RequestParam Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "30") int duracionSlot) {  // Duración en minutos de cada slot (por defecto 30).

        //Llama al servicio para generar los slots según los parámetros enviados.
        agendamientoService.generarSlotsDeDisponibilidad(veterinarioId, fechaInicio, fechaFin, duracionSlot);
        return ResponseEntity.ok().build();  // Respuesta HTTP 200 sin contenido.
    }

    // Expone un endpoint PATCH para modificar el estado de un slot.
    @PatchMapping("/slots/{slotId}/estado")  // ADMIN o VETERINARIO pueden cambiar estados.
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadResponseDTO> actualizarEstadoSlot(
            @PathVariable Long slotId,
            @RequestParam EstadoDisponibilidad nuevoEstado) {  // Nuevo estado: DISPONIBLE, OCUPADO, BLOQUEADO, etc.

        // Actualiza el estado del slot de forma manual usando el servicio
        return ResponseEntity.ok(agendamientoService.actualizarEstadoSlotManualmente(slotId, nuevoEstado));
    }


    // Endpoint GET para consultar disponibilidad por veterinario y fecha.
    @GetMapping("/vet/{veterinarioId}")
    @PreAuthorize("isAuthenticated()")  // Cualquier usuario autenticado puede consultar la disponibilidad.
    public ResponseEntity<List<DisponibilidadResponseDTO>> getDisponibilidad(
            @PathVariable Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        // Devuelve la lista de slots disponibles para ese veterinario en ese día.
        return ResponseEntity.ok(agendamientoService.consultarDisponibilidadPorFecha(veterinarioId, fecha));
    }


    // Endpoint POST para reservar uno o varios slots.
    @PostMapping("/reservar")
    @PreAuthorize("isAuthenticated()")   // Debe estar autenticado.
    public ResponseEntity<List<DisponibilidadResponseDTO>> reservarSlots(@RequestBody ReservaRequestDTO dto) {

        // Ejecuta la reserva de los slots enviados en el DTO.
        return ResponseEntity.ok(agendamientoService.reservarSlots(dto));
    }



    // Endpoint POST para liberar los slots asociados a una cita.
    @PostMapping("/liberar/{citaId}")
    @PreAuthorize("isAuthenticated()")  // Solo usuarios autenticados.
    public ResponseEntity<Void> liberarSlots(@PathVariable Long citaId) {

        // Llama al servicio para liberar los slots que fueron reservados por esa cita.
        agendamientoService.liberarSlotsPorCitaId(citaId);

        // Devuelve 200 OK sin contenido.
        return ResponseEntity.ok().build();
    }



    // Endpoint para obtener slots dado un listado de IDs.
    @PostMapping("/slots/list")
    @PreAuthorize("isAuthenticated()") // Requiere autenticación.
    public ResponseEntity<List<DisponibilidadResponseDTO>> getDisponibilidadByIds(@RequestBody List<Long> ids) {

        // Devuelve los slots correspondientes a los IDs enviados.
        return ResponseEntity.ok(agendamientoService.findDisponibilidadByIds(ids));
    }


    // Endpoint para obtener IDs de veterinarios por servicio.
    @GetMapping("/servicio/{servicioId}/veterinarios")
    @PreAuthorize("isAuthenticated()")  // Requiere autenticación.
    public ResponseEntity<List<Long>> getVeterinariosPorServicio(@PathVariable Long servicioId) {

        // Devuelve la lista de veterinarios que prestan el servicio especificado.
        return ResponseEntity.ok(agendamientoService.findVeterinarioIdsByServicioId(servicioId));
    }
}
