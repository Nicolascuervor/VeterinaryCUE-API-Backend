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

/**
 * Fábrica encargada de la creación polimórfica de usuarios.
 * Esta clase implementa el patrón de diseño Factory Method para centralizar y desacoplar
 * la lógica de instanciación de las distintas subclases de Usuario (Duenio, Veterinario, Admin).
 * En lugar de utilizar sentencias condicionales (if/switch) para determinar qué clase instanciar,
 * utiliza un mapa de estrategias (Strategy Pattern). Esto permite que el sistema sea fácilmente
 * extensible: para agregar un nuevo tipo de usuario, solo se necesita crear una nueva clase
 * que implemente la estrategia, sin modificar el código existente en esta fábrica.
 */
@Component
public class UsuarioFactory {

    /**
     * Mapa que asocia cada tipo de usuario (Enum) con su estrategia de creación correspondiente.
     * Se utiliza EnumMap por ser más eficiente en memoria y rendimiento para claves enumeradas.
     */
    private final Map<TipoUsuario, IUsuarioCreationStrategy> strategyMap;

    /**
     * Constructor que inyecta automáticamente todas las estrategias disponibles.
     * Spring Boot detecta todas las clases que implementan IUsuarioCreationStrategy
     * y las inyecta como una lista. Luego, el constructor puebla el mapa asociando
     * cada estrategia con el tipo de usuario que sabe manejar.
     */
    @Autowired
    public UsuarioFactory(List<IUsuarioCreationStrategy> strategies) {
        this.strategyMap = new EnumMap<>(TipoUsuario.class);

        for (IUsuarioCreationStrategy strategy : strategies) {
            this.strategyMap.put(strategy.getTipo(), strategy);
        }
    }

    /**
     * Crea una instancia específica de Usuario basada en los datos de registro.
     * Delega la creación a la estrategia correspondiente según el campo 'tipoUsuario' del DTO.
     */
    public Usuario crearUsuario(RegistroUsuarioDTO dto) {
        TipoUsuario tipo = dto.getTipoUsuario();

        // Buscamos la estrategia adecuada en el mapa pre-cargado
        IUsuarioCreationStrategy strategy = strategyMap.get(tipo);

        if (strategy == null) {
            throw new IllegalArgumentException("No se encontró una estrategia de creación para el tipo: " + tipo);
        }

        // Ejecutamos la estrategia para obtener la entidad construida
        return strategy.crearUsuario(dto);
    }

}
