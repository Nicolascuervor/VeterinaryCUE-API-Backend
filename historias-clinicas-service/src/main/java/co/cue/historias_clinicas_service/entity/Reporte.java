package co.cue.historias_clinicas_service.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reporte {
    // Texto que representa la parte superior del reporte
    private String encabezado;
    // Contenido principal del reporte, con detalles del historial clínico
    private String cuerpo;
    // Texto final del reporte, usado como cierre o notas adicionales
    private String pieDePagina;

    // Convierte el reporte en un texto legible y ordenado al imprimirlo
    @Override
    public String toString() {
        return "Reporte:\n" + encabezado + "\n" + cuerpo + "\n" + pieDePagina;
    }
}
