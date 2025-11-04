package co.cue.auth.models.entities;

import co.cue.auth.models.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "veterinarios")
@DiscriminatorValue("VETERINARIO")
@Getter
@Setter
@NoArgsConstructor
public class Veterinario extends Usuario {
    @Column(length = 100)
    private String especialidad;

    public Veterinario(String nombre, String apellido, String correo, String contraseña, String direccion, String telefono, String especialidad) {
        super(nombre, apellido, correo, contraseña, direccion, telefono, Role.ROLE_VETERINARIO);
        this.especialidad = especialidad;
    }
}
