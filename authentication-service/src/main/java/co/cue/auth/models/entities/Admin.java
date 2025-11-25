package co.cue.auth.models.entities;

import co.cue.auth.models.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa a un administrador del sistema.
 * Esta clase es una extensión de la entidad base Usuario.
 * Los administradores tienen acceso privilegiado a la gestión de la plataforma,
 * incluyendo la gestión de usuarios, inventario y configuración global.
 * Se distingue de otros usuarios por el valor discriminador "ADMIN" en la base de datos.
 */
@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
public class Admin extends Usuario {

    /**
     * Constructor especializado para la creación de Administradores.
     * Asigna automáticamente el rol ROLE_ADMIN y delega la inicialización
     * de los datos comunes al constructor de la clase padre (Usuario).
     */
    public Admin(String nombre, String apellido, String correo, String contrasenia, String direccion, String telefono) {
        super(nombre, apellido, correo, contrasenia, direccion, telefono, Role.ROLE_ADMIN);
    }
}
