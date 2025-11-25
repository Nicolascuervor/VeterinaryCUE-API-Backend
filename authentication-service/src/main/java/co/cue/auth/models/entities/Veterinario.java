package co.cue.auth.models.entities;

import co.cue.auth.models.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad que representa a un médico veterinario en el sistema.
 * Esta clase es una extensión de la entidad base Usuario.
 * Utiliza la estrategia de herencia Single Table o Joined (definida en el padre)
 * para almacenar los datos específicos de los doctores.
 * Se distingue de otros usuarios por el valor discriminador "VETERINARIO".
 */
@Entity
@Table(name = "veterinarios")
@DiscriminatorValue("VETERINARIO")
@Getter
@Setter
@NoArgsConstructor
public class Veterinario extends Usuario {

    /**
     * Especialidad médica del veterinario (ej. Cirujano, General, Dermatología).
     * Este campo es exclusivo para este tipo de usuario y permite filtrar
     * o asignar citas según la competencia del doctor.
     */
    @Column(length = 100)
    private String especialidad;

    /**
     * Constructor especializado para la creación de Veterinarios.
     * Asigna automáticamente el rol ROLE_VETERINARIO y delega la inicialización
     * de los datos comunes al constructor de la clase padre (Usuario).
     */
    public Veterinario(String nombre, String apellido, String correo, String contrasenia, String direccion, String telefono, String especialidad) {
        super(nombre, apellido, correo, contrasenia, direccion, telefono, Role.ROLE_VETERINARIO);
        this.especialidad = especialidad;
    }
}