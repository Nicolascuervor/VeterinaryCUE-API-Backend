package co.cue.auth.models.dtos;
import co.cue.auth.models.enums.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroUsuarioDTO {

    private String nombre;
    private String apellido;
    private String correo;
    private String contraseña;
    private String direccion;
    private String telefono;
    private TipoUsuario tipoUsuario;
    private String especialidad;

}
