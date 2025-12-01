package co.cue.agendamiento_service.controller;

import co.cue.agendamiento_service.models.entities.dtos.*;
import co.cue.agendamiento_service.services.IAgendamientoService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/agendamiento")
@AllArgsConstructor
public class AgendamientoController {

    private final IAgendamientoService agendamientoService;

    // ---------------------------------------------------
    // 1. CONFIGURACIÓN (Jornadas)
    // ---------------------------------------------------

    @PostMapping("/jornada")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<JornadaLaboralResponseDTO> configurarJornada(@RequestBody JornadaLaboralRequestDTO dto) {
        return ResponseEntity.ok(agendamientoService.crearActualizarJornada(dto));
    }

    // ---------------------------------------------------
    // 2. VISTA PARA EL FRONTEND (Calendario)
    // ---------------------------------------------------

    /**
     * Endpoint que devuelve lo necesario para pintar el calendario de un veterinario.
     * El Front recibe:
     * - Jornadas (para pintar el fondo blanco/gris).
     * - Ocupaciones (para pintar las citas/bloqueos encima).
     */
    @GetMapping("/calendario/{veterinarioId}")
    public ResponseEntity<AgendaVeterinarioDTO> obtenerAgenda(
            @PathVariable Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        AgendaVeterinarioDTO agenda = agendamientoService.obtenerAgendaParaCalendario(veterinarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(agenda);
    }

    // ---------------------------------------------------
    // 3. OPERACIONES DE BLOQUEO (Manual)
    // ---------------------------------------------------

    /**
     * Permite a un Admin o Veterinario bloquear un espacio manualmente.
     * Ej: "Tengo una reunión personal de 2pm a 3pm".
     */
    @PostMapping("/bloqueo")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<OcupacionResponseDTO> crearBloqueoManual(@RequestBody OcupacionRequestDTO dto) {
        // Forzamos el tipo si viene nulo, o validamos que no sea CITA (eso va por otro lado)
        return ResponseEntity.ok(agendamientoService.crearOcupacion(dto));
    }


    @GetMapping("/jornada/veterinario/{veterinarioId}")
    @PreAuthorize("isAuthenticated()") // Visible para todos los logueados
    public ResponseEntity<List<JornadaLaboralResponseDTO>> obtenerJornadasPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(agendamientoService.obtenerJornadasPorVeterinario(veterinarioId));
    }

    // ---------------------------------------------------
    // 4. API INTERNA (Para uso de citas-service)
    // ---------------------------------------------------

    /**
     * Llamado por citas-service cuando se crea una nueva cita.
     * Valida disponibilidad y reserva el espacio en una sola transacción.
     */
    @PostMapping("/interno/reservar")
    public ResponseEntity<OcupacionResponseDTO> reservarCita(@RequestBody OcupacionRequestDTO dto) {
        // Aquí la lógica asume que es una llamada de sistema confiable
        return ResponseEntity.ok(agendamientoService.crearOcupacion(dto));
    }

    /**
     * Llamado por citas-service cuando se cancela una cita.
     * Libera el espacio eliminando la ocupación.
     */
    @DeleteMapping("/interno/liberar/{referenciaId}")
    public ResponseEntity<Void> liberarCita(@PathVariable Long referenciaId) {
        agendamientoService.eliminarOcupacionPorReferencia(referenciaId);
        return ResponseEntity.noContent().build();
    }
}