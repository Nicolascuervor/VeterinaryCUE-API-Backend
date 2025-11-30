package co.cue.citas_service.pattern.state.concretestates;

import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.entity.EstadoCita;
import co.cue.citas_service.pattern.state.CitaStateAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EstadoConfirmada extends CitaStateAdapter {

    // Inicia la cita cambiando su estado a EN_PROGRESO
    @Override
    public void iniciar(Cita cita) {
        log.info("Iniciando cita {}...", cita.getId());
        cita.setEstado(EstadoCita.EN_PROGRESO);
    }

    // Cancela la cita cambiando su estado a CANCELADA
    @Override
    public void cancelar(Cita cita) {
        log.info("Cancelando cita confirmada {}...", cita.getId());
        cita.setEstado(EstadoCita.CANCELADA);
    }

    // Marca la cita como NO_ASISTIO si el paciente no asistió
    @Override
    public void noAsistio(Cita cita) {
        log.warn("El paciente no asistió a la cita {}.", cita.getId());
        cita.setEstado(EstadoCita.NO_ASISTIO);
    }
}
