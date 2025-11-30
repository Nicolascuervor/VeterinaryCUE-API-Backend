package co.cue.agendamiento_service.models.entities.enums;

public enum EstadoDisponibilidad {  // Enum que representa el estado de un slot de disponibilidad
    DISPONIBLE,    // El slot está disponible para ser reservado
    RESERVADO,     // El slot ya ha sido reservado por un cliente
    BLOQUEADO      // El slot está bloqueado y no puede ser reservado
}
