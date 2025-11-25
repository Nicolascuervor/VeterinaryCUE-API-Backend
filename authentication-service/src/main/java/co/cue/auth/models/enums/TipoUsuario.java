package co.cue.auth.models.enums;

/**
 * Enumeración que clasifica los tipos de usuarios desde una perspectiva de negocio y estructural.
 *
 * A diferencia del enum Role (que se enfoca en permisos de seguridad y autorización HTTP),
 * este enum se utiliza principalmente para:
 * 1. Determinar qué estrategia de creación de usuario ejecutar en el UsuarioFactory.
 * 2. Definir el valor de la columna discriminadora en la base de datos (InheritanceTypeJOINED).
 * 3. Validar datos de entrada específicos en el registro (ej. si es Veterinario, exigir especialidad).
 *
 * Es la pieza clave que conecta el DTO de registro con la lógica de creación polimórfica.
 */
public enum TipoUsuario {

    /**
     * Representa a un cliente externo o dueño de mascota.
     * Al seleccionarse, el sistema activará la DuenioCreationStrategy e instanciará la entidad Duenio.
     */
    DUENIO,

    /**
     * Representa a un profesional médico de la clínica.
     * Al seleccionarse, el sistema activará la VeterinarioCreationStrategy.
     * Este tipo requiere validaciones adicionales (ej. campo 'especialidad' obligatorio).
     */
    VETERINARIO,

    /**
     * Representa a un administrador del sistema.
     * Al seleccionarse, el sistema activará la AdminCreationStrategy.
     * Su uso suele estar restringido a endpoints protegidos.
     */
    ADMIN
}
