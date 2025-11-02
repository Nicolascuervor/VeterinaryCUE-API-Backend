package co.cue.auth.models.entities;

import co.cue.auth.models.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
public class Admin extends Usuario {
    public Admin(String nombre, String apellido, String correo, String contrasenia, String direccion, String telefono) {

        super(nombre, apellido, correo, contrasenia, direccion, telefono, Role.ROLE_ADMIN);
    }
}
