package co.cue.mascotas_service.exceptions;

// Excepción personalizada lanzada cuando no se encuentra una mascota por su ID.
public class MascotaNotFoundException extends RuntimeException {

    // Construye la excepción con un mensaje indicando el ID que no fue encontrado.
    public MascotaNotFoundException(Long id) {
        super("Mascota no encontrada con ID: " + id);
    }
}
