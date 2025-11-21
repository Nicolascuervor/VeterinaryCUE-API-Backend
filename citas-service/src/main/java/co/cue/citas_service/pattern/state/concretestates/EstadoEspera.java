package co.cue.citas_service.pattern.state.concretestates;

import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.entity.EstadoCita;
import co.cue.citas_service.pattern.state.CitaStateAdapter;
import org.springframework.stereotype.Component;

@Component
public class EstadoEspera extends CitaStateAdapter {

    @Override
    public void confirmar(Cita cita) {
        cita.setEstado(EstadoCita.CONFIRMADA);
    }

    @Override
    public void cancelar(Cita cita) {
        cita.setEstado(EstadoCita.CANCELADA);
    }
}
