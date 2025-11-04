package co.cue.auth.models.entities;
import co.cue.auth.models.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "dueños")
@DiscriminatorValue("DUEÑO")
@Getter
@Setter
@NoArgsConstructor

public class Duenio extends Usuario {
    public Duenio(String nombre, String apellido, String correo, String contraseña, String direccion, String telefono) {
        super(nombre, apellido, correo, contraseña, direccion, telefono, Role.ROLE_DUEÑO);
    }
}
