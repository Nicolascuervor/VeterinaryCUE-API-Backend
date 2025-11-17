package co.cue.historias_clinicas_service.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reporte {
    private String encabezado;
    private String cuerpo;
    private String pieDePagina;

    @Override
    public String toString() {
        return "Reporte:\n" + encabezado + "\n" + cuerpo + "\n" + pieDePagina;
    }
}
