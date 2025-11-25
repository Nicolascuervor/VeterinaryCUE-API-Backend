package co.cue.auth.models.enums;

/**
 * Enumeración que define los roles de seguridad disponibles en el sistema.
 *
 * Estos valores se utilizan para:
 * 1. Asignar permisos a los usuarios en el momento del registro.
 * 2. Configurar las reglas de autorización en SecurityConfig (quién puede acceder a qué ruta).
 * 3. Generar los "authorities" dentro del token JWT.
 *
 * Nota: Se utiliza el prefijo "ROLE_" por convención de Spring Security, aunque
 * en nuestra configuración actual hemos ajustado el JwtAuthenticationConverter para
 * manejarlo de forma flexible.
 */
public enum Role {

    /**
     * Rol para clientes y dueños de mascotas.
     *
     * Permisos típicos:
     * - Ver sus propias mascotas e historias clínicas.
     * - Agendar citas.
     * - Realizar pedidos en la tienda.
     * - Ver y pagar sus facturas.
     */
    ROLE_DUENIO,

    /**
     * Rol para el personal médico veterinario.
     *
     * Permisos típicos:
     * - Ver y editar historias clínicas de cualquier mascota.
     * - Gestionar su propia disponibilidad y agenda.
     * - Atender citas (cambiar estado a EN_PROGRESO/FINALIZADA).
     */
    ROLE_VETERINARIO,

    /**
     * Rol de administrador del sistema.
     *
     * Permisos típicos:
     * - Gestión total de usuarios (activar/desactivar).
     * - Configuración del catálogo de servicios y precios.
     * - Gestión de inventario (crear productos, reponer stock).
     * - Ver reportes globales de facturación.
     */
    ROLE_ADMIN
}
