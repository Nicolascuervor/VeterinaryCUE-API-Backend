package co.cue.auth.models.entities;
import co.cue.auth.models.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad que representa a un cliente o dueño de mascota en el sistema.
 * Esta clase es una extensión de la entidad base Usuario.
 * Es el actor principal que solicitará servicios (Citas, Pedidos) y
 * registrará a sus mascotas.
 * Se distingue de otros usuarios por el valor discriminador "DUENIO".
 * Aunque actualmente no tiene atributos adicionales respecto a Usuario,
 * mantenerla como entidad separada permite escalabilidad futura (ej. saldo de puntos,
 * nivel de membresía) sin alterar la tabla principal.
 */
@Entity
@Table(name = "duenios")
@DiscriminatorValue("DUENIO")
@Getter
@Setter
@NoArgsConstructor
public class Duenio extends Usuario {

    /**
     * Constructor especializado para la creación de Dueños.
     * Asigna automáticamente el rol ROLE_DUENIO y delega la inicialización
     * de los datos comunes al constructor de la clase padre (Usuario).
     */
    public Duenio(String nombre, String apellido, String correo, String contrasenia, String direccion, String telefono) {
        super(nombre, apellido, correo, contrasenia, direccion, telefono, Role.ROLE_DUENIO);
    }
}
