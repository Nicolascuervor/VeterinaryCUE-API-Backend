package co.cue.auth.strategies;

import co.cue.auth.models.dtos.RegistroUsuarioDTO;
import co.cue.auth.models.entities.Usuario;
import co.cue.auth.models.enums.TipoUsuario;

/**
 * Interfaz que define la estrategia para la creación de usuarios.
 * Implementa el patrón de diseño Strategy. Cada clase que implemente esta interfaz
 * encapsulará la lógica específica necesaria para instanciar y configurar un tipo
 * concreto de usuario (Veterinario, Dueño, Admin).
 * Esto permite que la fábrica (UsuarioFactory) delegue la creación sin conocer
 * los detalles de implementación de cada subclase, cumpliendo con el principio
 * de Responsabilidad Única y Abierto/Cerrado (SOLID).
 */
public interface IUsuarioCreationStrategy {

    /**
     * Crea una instancia específica de una entidad de usuario.
     * Recibe los datos de transferencia (DTO) y los convierte en una entidad
     * JPA concreta. Aquí es donde se pueden aplicar reglas de validación
     * específicas del tipo (por ejemplo, asegurar que un Veterinario tenga especialidad)
     * antes de retornar el objeto listo para ser guardado.
     */
    Usuario crearUsuario(RegistroUsuarioDTO dto);

    /**
     * Obtiene el identificador del tipo de usuario que maneja esta estrategia.
     * Este método es utilizado por el UsuarioFactory para registrar esta estrategia
     * en su mapa interno. Actúa como la "clave" que asocia un valor del enum
     * TipoUsuario con esta implementación concreta.
     */
    TipoUsuario getTipo();
}