package co.cue.historias_clinicas_service.service;

import co.cue.historias_clinicas_service.entity.HistorialClinico;
import co.cue.historias_clinicas_service.entity.Reporte;
import co.cue.historias_clinicas_service.patterns.builder.HistorialClinicoReporteBuilder;
import co.cue.historias_clinicas_service.patterns.builder.ReporteDirector;
import co.cue.historias_clinicas_service.repository.HistorialClinicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReporteService {

    private final ReporteDirector reporteDirector;

    private final HistorialClinicoReporteBuilder reporteBuilder;

    private final HistorialClinicoRepository historialRepository;

    @Transactional(readOnly = true)
    public Reporte generarReporteDeHistorial(Long historialId, Long usuarioId) {
        HistorialClinico historial = historialRepository.findById(historialId)
                .orElseThrow(() -> new EntityNotFoundException("Historial no encontrado: " + historialId));
        reporteBuilder.setHistorial(historial);
        reporteDirector.construirReporteCompleto(reporteBuilder);
        return reporteBuilder.getReporte();
    }
}
