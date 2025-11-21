package co.cue.citas_service.pattern.state;

import co.cue.citas_service.entity.Cita;

public interface ICitaState {
    void confirmar(Cita cita);
    void iniciar(Cita cita);
    void finalizar(Cita cita);
    void cancelar(Cita cita);
    void noAsistio(Cita cita);
}
