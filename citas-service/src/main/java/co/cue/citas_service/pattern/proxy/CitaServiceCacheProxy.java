package co.cue.citas_service.pattern.proxy;
import co.cue.citas_service.dtos.CitaRequestDTO;
import co.cue.citas_service.dtos.CitaResponseDTO;
import co.cue.citas_service.dtos.CitaUpdateDTO;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.service.ICitaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
@Primary
@Slf4j
public class CitaServiceCacheProxy implements ICitaService {


    private final ICitaService realService;

    private final Map<LocalDate, List<CitaResponseDTO>> cache = new ConcurrentHashMap<>();


    @Autowired
    public CitaServiceCacheProxy(@Qualifier("citaServiceImpl") ICitaService realService) {
        this.realService = realService;
    }



    @Override
    public List<Cita> findCitaByEstado(String estado) {
        log.debug("PROXY CACHE: Passthrough findCitaByEstado");
        return realService.findCitaByEstado(estado);
    }

    @Override
    public CitaResponseDTO findCitaById(Long id) {
        log.debug("PROXY CACHE: Passthrough findCitaById");
        return realService.findCitaById(id);
    }


    @Override
    public CitaResponseDTO createCita(CitaRequestDTO citaDTO, Long usuarioId) {
        log.debug("PROXY CACHE: (Write) Delegando createCita al servicio real...");
        CitaResponseDTO nuevaCita = realService.createCita(citaDTO, usuarioId);
        log.info("PROXY CACHE: (Invalidate 🗑️) Creación detectada. Limpiando caché para {}.", LocalDate.now());
        cache.remove(LocalDate.now());

        return nuevaCita;
    }

    @Override
    public CitaUpdateDTO updateCita(Long id, CitaUpdateDTO citaUpdateDTO) {
        log.debug("PROXY CACHE: (Write) Delegando updateCita al servicio real...");
        CitaUpdateDTO citaActualizada = realService.updateCita(id, citaUpdateDTO);
        log.info("PROXY CACHE: (Invalidate 🗑️) Actualización detectada. Limpiando caché para {}.", LocalDate.now());
        cache.remove(LocalDate.now());
        return citaActualizada;
    }

    @Override
    public void deleteCita(Long id) {
        log.debug("PROXY CACHE: (Write) Delegando deleteCita al servicio real...");
        realService.deleteCita(id);
        log.info("PROXY CACHE: (Invalidate 🗑️) Eliminación detectada. Limpiando caché para {}.", LocalDate.now());
        cache.remove(LocalDate.now());
    }

    @Override
    public List<CitaResponseDTO> findCitasDelDia(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        if (!fecha.equals(hoy)) {
            log.warn("PROXY CACHE: (Bypass) Solicitud para fecha {} no es 'hoy'. Delegando a BD.", fecha);
            return realService.findCitasDelDia(fecha);
        }
        List<CitaResponseDTO> citasCacheadas = cache.get(hoy);
        if (citasCacheadas != null) {
            log.info("PROXY CACHE: (Cache Hit) Devolviendo citas del día desde caché.");
            return citasCacheadas;
        }
        log.info("PROXY CACHE: (Cache Miss) Buscando citas del día en el servicio real (BD).");
        List<CitaResponseDTO> citasReales = realService.findCitasDelDia(hoy);
        cache.put(hoy, citasReales);
        return citasReales;
    }
}
