package co.cue.auth.models.entities;
import co.cue.auth.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


/**
 * Entidad base que representa a cualquier usuario autenticado en el sistema.
 * Esta clase cumple dos funciones arquitectónicas críticas:
 * Persistencia (JPA): Define la tabla 'usuarios' que almacena los datos comunes
 * (nombre, correo, contraseña) compartidos por Dueños, Veterinarios y Administradores.
 * Utiliza la estrategia InheritanceType JOINED lo que significa que habrá una tabla
 * física para esta clase y tablas separadas para cada subclase, unidas por FK.
 * Seguridad (Spring Security): Implementa la interfaz UserDetails permitiendo
 * que los objetos de esta clase sean utilizados directamente por el AuthenticationManager
 * para validar credenciales y roles.
 */
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de tabla por subclase (normalizada)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING) // Columna que diferencia el tipo real
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos Personales e Identificación
    // Información básica requerida para todos los actores del sistema.

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    @Email(message = "El formato del correo electrónico es inválido (debe contener @ y dominio)")
    private String correo;

    @Column(nullable = false)
    private String contrasenia;

    // Información de Contacto

    @Column(length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    // Control de Acceso y Estado

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role userRole;

    @Column(nullable = false)
    private boolean activo;

    // Auditoría
    // Campos gestionados automáticamente por Hibernate para rastrear el ciclo de vida del registro.

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Constructor parametrizado para facilitar la creación desde las estrategias (Factory Pattern).
     * Inicializa el usuario como 'activo' por defecto.
     */
    public Usuario(String nombre, String apellido, String correo, String contrasenia, String direccion, String telefono, Role userRole) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.direccion = direccion;
        this.telefono = telefono;
        this.userRole = userRole;
        this.activo = true;
    }


    /**
     * Retorna los permisos (authorities) otorgados al usuario.
     * En este diseño, mapeamos directamente él userRole (Enum) a una autoridad de Spring.
     * Esto permite usar anotaciones como PreAuthorize("hasRole('ADMIN')") en los controladores.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    /**
     * Retorna la contraseña utilizada para la autenticación.
     * Es el hash BCrypt almacenado en la base de datos.
     */
    @Override
    public String getPassword() {
        return this.contrasenia;
    }

    /**
     * Retorna el nombre de usuario utilizado para la autenticación.
     * En nuestro sistema, el identificador único es el correo electrónico.
     */
    @Override
    public String getUsername() {
        return this.correo;
    }

    // Flags de Estado de Cuenta
    // Estos métodos permiten a Spring Security bloquear el acceso si la cuenta tiene problemas.
    // Por ahora, simplificamos retornando 'true' en la mayoría, delegando el bloqueo solo al campo 'activo'.

    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta nunca expira automáticamente por fecha.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // No implementamos bloqueo por intentos fallidos aún.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales (contraseña) no caducan forzosamente.
    }

    /**
     * Indica si el usuario está habilitado o deshabilitado.
     * Vinculamos este método al campo activo de nuestra base de datos.
     * Si esto retorna  false, Spring Security rechazará el login automáticamente,
     * lo cual es útil para el Soft Delete que implementamos.
     */
    @Override
    public boolean isEnabled() {
        return this.activo;
    }
}
