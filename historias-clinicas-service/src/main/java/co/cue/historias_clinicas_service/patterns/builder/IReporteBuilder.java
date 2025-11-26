package co.cue.historias_clinicas_service.patterns.builder;

import co.cue.historias_clinicas_service.entity.Reporte;

// Interfaz del patrón Builder que define los pasos
// necesarios para construir un Reporte

public interface IReporteBuilder {
    // Construye el encabezado del reporte
    void construirEncabezado();
    // Construye el cuerpo del reporte
    void construirCuerpo();
    // Construye el pie de página del reporte
    void construirPieDePagina();
    // Retorna el objeto Reporte ya construido
    Reporte getReporte();
}
