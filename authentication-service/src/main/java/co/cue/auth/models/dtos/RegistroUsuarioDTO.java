package co.cue.auth.models.dtos;
import co.cue.auth.models.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroUsuarioDTO {

    private String nombre;
    private String apellido;

    @Email(message = "El formato del correo electrónico es inválido (debe contener @ y dominio)") // <-- ¡Aquí está la magia!
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = ".*\\d.*", message = "La contraseña debe contener al menos un número")
    private String contrasenia;

    private String direccion;
    private String telefono;
    private TipoUsuario tipoUsuario;
    private String especialidad;

}
