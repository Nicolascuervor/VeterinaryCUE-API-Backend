package co.cue.auth.models.dtos;
import co.cue.auth.models.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para el registro de nuevos usuarios.
 * Este objeto encapsula todos los datos necesarios para crear una nueva cuenta en el sistema.
 * Actúa como la primera línea de defensa en la seguridad de la aplicación, implementando
 * validaciones estrictas (Jakarta Validation) sobre los datos de entrada.
 * Si alguno de los campos no cumple con las restricciones (ej. contraseña corta o correo inválido),
 * el framework rechazará la petición automáticamente con un error 400 (Bad Request) antes de
 * invocar la lógica de negocio, protegiendo así la integridad de la base de datos.
 */
@Getter
@Setter
public class RegistroUsuarioDTO {

    // Información Personal

    /**
     * Nombre de pila del usuario.
     * Campo obligatorio para la personalización de correos y UI.
     */
    private String nombre;

    /**
     * Apellido(s) del usuario.
     */
    private String apellido;

    // Credenciales y Seguridad

    /**
     * Correo electrónico del usuario.
     * Se utiliza la anotación Email para validar que la cadena tenga formato de correo
     * usuario@dominio.com. Esto previene errores de notificación y asegura que el username
     * sea válido desde el inicio.
     */
    @Email(message = "El formato del correo electrónico es inválido (debe contener @ y dominio)")
    private String correo;

    /**
     * Contraseña en texto plano para la nueva cuenta.
     * Aplicamos reglas de complejidad para forzar una seguridad mínima:
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = ".*\\d.*", message = "La contraseña debe contener al menos un número")
    private String contrasenia;

    //Información de Contacto

    private String direccion;
    private String telefono;

    // Clasificación y Roles

    /**
     * Tipo de usuario a crear (DUEÑO, VETERINARIO, ADMIN).
     * Este enum es fundamental, ya que el UsuarioFactory lo utilizará para decidir
     * qué estrategia de creación ejecutar y qué entidad instanciar.
     */
    private TipoUsuario tipoUsuario;

    /**
     * Especialidad médica (Campo Condicional).
     * Este campo solo es obligatorio y procesado si tipoUsuario es VETERINARIO.
     * Para otros tipos de usuario, este valor será ignorado por la lógica de negocio.
     */
    private String especialidad;

}
