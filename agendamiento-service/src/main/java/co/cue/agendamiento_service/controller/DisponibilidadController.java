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

@RestController
@RequestMapping("/api/agendamiento/disponibilidad")
@AllArgsConstructor
public class DisponibilidadController {

    private final IAgendamientoService agendamientoService;


    @PostMapping("/jornada")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<JornadaLaboralResponseDTO> crearActualizarJornada(@RequestBody JornadaLaboralRequestDTO dto) {
        return ResponseEntity.ok(agendamientoService.crearActualizarJornada(dto));
    }


    @PostMapping("/generar-slots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> generarSlots(
            @RequestParam Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "30") int duracionSlot) {

        agendamientoService.generarSlotsDeDisponibilidad(veterinarioId, fechaInicio, fechaFin, duracionSlot);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/slots/{slotId}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadResponseDTO> actualizarEstadoSlot(
            @PathVariable Long slotId,
            @RequestParam EstadoDisponibilidad nuevoEstado) {

        return ResponseEntity.ok(agendamientoService.actualizarEstadoSlotManualmente(slotId, nuevoEstado));
    }


    @GetMapping("/vet/{veterinarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DisponibilidadResponseDTO>> getDisponibilidad(
            @PathVariable Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return ResponseEntity.ok(agendamientoService.consultarDisponibilidadPorFecha(veterinarioId, fecha));
    }


    @PostMapping("/reservar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DisponibilidadResponseDTO>> reservarSlots(@RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.ok(agendamientoService.reservarSlots(dto));
    }


    @PostMapping("/liberar/{citaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> liberarSlots(@PathVariable Long citaId) {
        agendamientoService.liberarSlotsPorCitaId(citaId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/slots/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DisponibilidadResponseDTO>> getDisponibilidadByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(agendamientoService.findDisponibilidadByIds(ids));
    }

    @GetMapping("/servicio/{servicioId}/veterinarios")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Long>> getVeterinariosPorServicio(@PathVariable Long servicioId) {
        return ResponseEntity.ok(agendamientoService.findVeterinarioIdsByServicioId(servicioId));
    }
}
