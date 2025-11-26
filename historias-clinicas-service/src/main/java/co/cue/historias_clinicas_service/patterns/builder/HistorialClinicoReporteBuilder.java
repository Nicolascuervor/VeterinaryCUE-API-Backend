package co.cue.historias_clinicas_service.patterns.builder;

import co.cue.historias_clinicas_service.entity.HistorialClinico;
import co.cue.historias_clinicas_service.entity.Reporte;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class HistorialClinicoReporteBuilder implements IReporteBuilder {

    private Reporte reporte;
    private HistorialClinico historial;

    public HistorialClinicoReporteBuilder() {
        this.reporte = new Reporte();
    }

    // Asigna el historial clínico y reinicia el reporte
    public void setHistorial(HistorialClinico historial) {
        this.historial = historial;
        this.reporte = new Reporte();
    }

    @Override
    public void construirEncabezado() {
        reporte.setEncabezado("--- REPORTE HISTORIA CLÍNICA ---" +
                "\nFecha: " + historial.getFecha() +
                "\nMascota ID: " + historial.getPetId() +
                "\nVeterinario ID: " + historial.getVeterinarianId());
    }

    @Override
    public void construirCuerpo() {
        reporte.setCuerpo("\n--- Diagnóstico ---\n" + historial.getDiagnostico() +
                "\n\n--- Tratamiento ---\n" + historial.getTratamiento() +
                "\n\n--- Observaciones ---\n" + historial.getObservaciones());
    }

    @Override
    public void construirPieDePagina() {
        // Se usa un bloque de texto (text block) para evitar problemas de comillas
        reporte.setPieDePagina( """
        <html>
            <body>
                <tag>
                \\n\\n--- Fin del Reporte ---" +  "\\n© Veterinaria CUE
                </tag>
            </body>
        </html>""");
    }

    @Override
    public Reporte getReporte() {
        return this.reporte;
    }
}