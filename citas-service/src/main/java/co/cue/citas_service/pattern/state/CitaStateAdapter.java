package co.cue.citas_service.pattern.state;

import co.cue.citas_service.entity.Cita;

public abstract class CitaStateAdapter implements ICitaState {

    @Override
    public void confirmar(Cita cita) {
        throw new IllegalStateException("No se puede CONFIRMAR una cita desde el estado " + cita.getEstado());
    }

    @Override
    public void iniciar(Cita cita) {
        throw new IllegalStateException("No se puede INICIAR una cita desde el estado " + cita.getEstado());
    }

    @Override
    public void finalizar(Cita cita) {
        throw new IllegalStateException("No se puede FINALIZAR una cita desde el estado " + cita.getEstado());
    }

    @Override
    public void cancelar(Cita cita) {
        throw new IllegalStateException("No se puede CANCELAR una cita desde el estado " + cita.getEstado());
    }

    @Override
    public void noAsistio(Cita cita) {
        throw new IllegalStateException("No se puede marcar NO ASISTIÓ desde el estado " + cita.getEstado());
    }
}
