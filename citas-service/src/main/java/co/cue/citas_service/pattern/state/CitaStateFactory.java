package co.cue.citas_service.pattern.state;

import co.cue.citas_service.entity.EstadoCita;
import co.cue.citas_service.pattern.state.concretestates.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class CitaStateFactory {

    // Instancias de los diferentes estados concretos
    private final EstadoEspera estadoEspera;
    private final EstadoConfirmada estadoConfirmada;
    private final EstadoEnProgreso estadoEnProgreso;
    private final EstadoFinalizada estadoFinalizada;
    private final EstadoCancelada estadoCancelada;
    private final EstadoNoAsistio estadoNoAsistio;

    // Mapa que relaciona EstadoCita con su implementación de ICitaState
    private final Map<EstadoCita, ICitaState> stateMap = new EnumMap<>(EstadoCita.class);

    // Inicializa el mapa de estados después de construir el bean
    @PostConstruct
    public void init() {
        stateMap.put(EstadoCita.ESPERA, estadoEspera);
        stateMap.put(EstadoCita.CONFIRMADA, estadoConfirmada);
        stateMap.put(EstadoCita.EN_PROGRESO, estadoEnProgreso);
        stateMap.put(EstadoCita.FINALIZADA, estadoFinalizada);
        stateMap.put(EstadoCita.CANCELADA, estadoCancelada);
        stateMap.put(EstadoCita.NO_ASISTIO, estadoNoAsistio);
    }

    // Devuelve la implementación de ICitaState correspondiente a un EstadoCita
    public ICitaState getState(EstadoCita estado) {
        ICitaState state = stateMap.get(estado);
        if (state == null) {
            throw new IllegalArgumentException("No se ha implementado la lógica para el estado: " + estado);
        }
        return state;
    }
}
