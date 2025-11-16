package co.cue.auth.models.entities;
import co.cue.auth.models.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "duenios")
@DiscriminatorValue("DUENIO")
@Getter
@Setter
@NoArgsConstructor

public class Duenio extends Usuario {
    public Duenio(String nombre, String apellido, String correo, String contrasenia, String direccion, String telefono) {
        super(nombre, apellido, correo, contrasenia, direccion, telefono, Role.ROLE_DUENIO);
    }
}
