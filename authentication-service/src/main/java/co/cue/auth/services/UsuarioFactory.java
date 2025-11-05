package co.cue.auth.services;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.enums.TipoUsuario;
import co.cue.auth.strategies.IUsuarioCreationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
@Component
public class UsuarioFactory {

    private final Map<TipoUsuario, IUsuarioCreationStrategy> strategyMap;

    @Autowired
    public UsuarioFactory(List<IUsuarioCreationStrategy> strategies) {
        this.strategyMap = new EnumMap<>(TipoUsuario.class);

        for (IUsuarioCreationStrategy strategy : strategies) {
            this.strategyMap.put(strategy.getTipo(), strategy);
        }
    }

    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        TipoUsuario tipo = dto.getTipoUsuario();
        IUsuarioCreationStrategy strategy = strategyMap.get(tipo);
        if (strategy == null) {
            throw new IllegalArgumentException("No se encontró una estrategia de creación para el tipo: " + tipo);
        }
        return strategy.crearUsuario(dto);
    }

}
