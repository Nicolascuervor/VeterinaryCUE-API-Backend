package co.cue.agendamiento_service.models.entities.enums;

public enum TipoOcupacion {
    /**
     * Una cita médica agendada a través del sistema.
     * Vinculada a un registro en citas-service.
     */
    CITA,

    /**
     * Bloqueo manual por parte del administrativo o veterinario.
     * Ej: "Reunión de equipo", "Permiso personal".
     */
    BLOQUEO_ADMINISTRATIVO,

    /**
     * Tiempo de descanso o almuerzo flexible.
     */
    DESCANSO,

    /**
     * Periodo largo de ausencia (días completos).
     */
    VACACIONES,

    /**
     * El servicio o consultorio está cerrado por mantenimiento.
     */
    MANTENIMIENTO // Quirófano cerrado, etc.
}
