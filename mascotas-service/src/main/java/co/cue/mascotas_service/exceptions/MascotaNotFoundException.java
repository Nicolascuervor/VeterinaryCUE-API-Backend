package co.cue.mascotas_service.exceptions;

public class MascotaNotFoundException extends RuntimeException {
    public MascotaNotFoundException(Long id) {
        super("Mascota no encontrada con ID: " + id);
    }
}
