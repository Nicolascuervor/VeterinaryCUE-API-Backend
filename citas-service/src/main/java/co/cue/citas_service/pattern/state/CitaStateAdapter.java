package co.cue.citas_service.pattern.state;

import co.cue.citas_service.entity.Cita;

// Adaptador base para los diferentes estados de una cita
// Implementa ICitaState y lanza excepciones por defecto para métodos no permitidos

public abstract class CitaStateAdapter implements ICitaState {

    // Intentar confirmar una cita desde un estado no permitido
    @Override
    public void confirmar(Cita cita) {
        throw new IllegalStateException("No se puede CONFIRMAR una cita desde el estado " + cita.getEstado());
    }

    // Intentar iniciar una cita desde un estado no permitido
    @Override
    public void iniciar(Cita cita) {
        throw new IllegalStateException("No se puede INICIAR una cita desde el estado " + cita.getEstado());
    }

    // Intentar finalizar una cita desde un estado no permitido
    @Override
    public void finalizar(Cita cita) {
        throw new IllegalStateException("No se puede FINALIZAR una cita desde el estado " + cita.getEstado());
    }

    // Intentar cancelar una cita desde un estado no permitido
    @Override
    public void cancelar(Cita cita) {
        throw new IllegalStateException("No se puede CANCELAR una cita desde el estado " + cita.getEstado());
    }

    // Intentar marcar una cita como NO ASISTIÓ desde un estado no permitido

    @Override
    public void noAsistio(Cita cita) {
        throw new IllegalStateException("No se puede marcar NO ASISTIÓ desde el estado " + cita.getEstado());
    }
}
