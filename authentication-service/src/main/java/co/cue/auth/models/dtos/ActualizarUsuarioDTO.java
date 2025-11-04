package co.cue.auth.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarUsuarioDTO {
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;

    // Si un veterinario puede cambiar su especialidad
    private String especialidad;
}
