package co.cue.historias_clinicas_service.patterns.builder;

import co.cue.historias_clinicas_service.entity.Reporte;

public interface IReporteBuilder {
    void construirEncabezado();
    void construirCuerpo();
    void construirPieDePagina();
    Reporte getReporte();
}
