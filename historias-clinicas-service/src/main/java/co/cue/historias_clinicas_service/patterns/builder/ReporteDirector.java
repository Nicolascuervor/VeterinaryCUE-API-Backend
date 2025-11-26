package co.cue.historias_clinicas_service.patterns.builder;

import org.springframework.stereotype.Component;

@Component
public class ReporteDirector {

    public void construirReporteCompleto(IReporteBuilder builder) {
        builder.construirEncabezado();// Construcción del encabezado del reporte
        builder.construirCuerpo();// Construcción del cuerpo principal del reporte
        builder.construirPieDePagina();// Construcción del pie de página del reporte
    }
}
