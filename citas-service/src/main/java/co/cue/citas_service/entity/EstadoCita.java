package co.cue.citas_service.entity;
// Enum que representa los posibles estados de una cita
public enum EstadoCita {

    // Cita en espera de confirmación
    ESPERA,

    // Cita confirmada
    CONFIRMADA,

    // Cita que está en progreso
    EN_PROGRESO,

    // Cita cancelada
    CANCELADA,

    // Cita finalizada
    FINALIZADA,

    // El paciente no asistió a la cita
    NO_ASISTIO
}
