package co.cue.citas_service.pattern.state.concretestates;

import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.entity.EstadoCita;
import co.cue.citas_service.pattern.state.CitaStateAdapter;
import org.springframework.stereotype.Component;

@Component
public class EstadoEnProgreso extends CitaStateAdapter {

    @Override
    public void finalizar(Cita cita) {
        cita.setEstado(EstadoCita.FINALIZADA);
    }

}