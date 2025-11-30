package co.cue.citas_service.pattern.state;

import co.cue.citas_service.entity.Cita;

// Interfaz que define las acciones posibles para los diferentes estados de una cita
public interface ICitaState {

    // Confirma la cita
    void confirmar(Cita cita);

    // Inicia la cita
    void iniciar(Cita cita);

    // Finaliza la cita
    void finalizar(Cita cita);

    // Cancela la cita
    void cancelar(Cita cita);

    // Marca la cita como no asistida
    void noAsistio(Cita cita);
}
