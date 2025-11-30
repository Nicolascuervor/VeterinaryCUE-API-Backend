package co.cue.citas_service.pattern.state.concretestates;

import co.cue.citas_service.pattern.state.CitaStateAdapter;
import org.springframework.stereotype.Component;

@Component
public class EstadoCancelada extends CitaStateAdapter {
    // Clase que representa el estado "Cancelada" de una cita
    // Actualmente hereda la funcionalidad por defecto de CitaStateAdapter
}
