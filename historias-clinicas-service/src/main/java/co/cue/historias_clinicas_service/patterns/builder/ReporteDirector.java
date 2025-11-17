package co.cue.historias_clinicas_service.patterns.builder;

import org.springframework.stereotype.Component;

@Component
public class ReporteDirector {


    public void construirReporteCompleto(IReporteBuilder builder) {
        builder.construirEncabezado();
        builder.construirCuerpo();
        builder.construirPieDePagina();
    }


     public void construirReporteResumido(IReporteBuilder builder) {
         builder.construirEncabezado();
        builder.construirCuerpo();
     }
}
