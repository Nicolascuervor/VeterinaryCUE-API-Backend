package co.cue.citas_service.pattern.proxy;
import co.cue.citas_service.dtos.CitaDetailDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
@Primary
@Slf4j
public class CitaServiceCacheProxy implements ICitaService {

    // Servicio real que se delega
    private final ICitaService realService;

    // Caché de citas por fecha
    private final Map<LocalDate, List<CitaResponseDTO>> cache = new ConcurrentHashMap<>();

    // Constructor inyectando el servicio real
    @Autowired
    public CitaServiceCacheProxy(@Qualifier("citaServiceImpl") ICitaService realService) {
        this.realService = realService;
    }


    // Buscar citas por estado
    @Override
    public List<Cita> findCitaByEstado(String estado) {
        log.debug("PROXY CACHE: Passthrough findCitaByEstado");
        return realService.findCitaByEstado(estado);
    }

    // Buscar cita por id
    @Override
    public CitaResponseDTO findCitaById(Long id) {
        log.debug("PROXY CACHE: Passthrough findCitaById");
        return realService.findCitaById(id);
    }

    // Crear nueva cita y limpiar caché del día
    @Override
    public CitaResponseDTO createCita(CitaRequestDTO citaDTO, Long usuarioId) {
        log.debug("PROXY CACHE: (Write) Delegando createCita al servicio real...");
        CitaResponseDTO nuevaCita = realService.createCita(citaDTO, usuarioId);
        log.info("PROXY CACHE: (Invalidate 🗑️) Creación detectada. Limpiando caché para {}.", LocalDate.now());
        cache.remove(LocalDate.now());

        return nuevaCita;
    }

    // Actualizar cita y limpiar caché del día
    @Override
    public CitaUpdateDTO updateCita(Long id, CitaUpdateDTO citaUpdateDTO) {
        log.debug("PROXY CACHE: (Write) Delegando updateCita al servicio real...");
        CitaUpdateDTO citaActualizada = realService.updateCita(id, citaUpdateDTO);
        log.info("PROXY CACHE: (Invalidate 🗑️) Actualización detectada. Limpiando caché para {}.", LocalDate.now());
        cache.remove(LocalDate.now());
        return citaActualizada;
    }

    // Eliminar cita y limpiar caché del día
    @Override
    public void deleteCita(Long id) {
        log.debug("PROXY CACHE: (Write) Delegando deleteCita al servicio real...");
        realService.deleteCita(id);
        log.info("PROXY CACHE: (Invalidate 🗑️) Eliminación detectada. Limpiando caché para {}.", LocalDate.now());
        cache.remove(LocalDate.now());
    }

    // Buscar citas del día con caché
    @Override
    public List<CitaResponseDTO> findCitasDelDia(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        // Si la fecha no es hoy, delegar directamente al servicio real
        if (!fecha.equals(hoy)) {
            log.warn("PROXY CACHE: (Bypass) Solicitud para fecha {} no es 'hoy'. Delegando a BD.", fecha);
            return realService.findCitasDelDia(fecha);
        }
        // Revisar caché
        List<CitaResponseDTO> citasCacheadas = cache.get(hoy);
        if (citasCacheadas != null) {
            log.info("PROXY CACHE: (Cache Hit) Devolviendo citas del día desde caché.");
            return citasCacheadas;
        }
        // Cache miss, obtener del servicio real y guardar en caché
        log.info("PROXY CACHE: (Cache Miss) Buscando citas del día en el servicio real (BD).");
        List<CitaResponseDTO> citasReales = realService.findCitasDelDia(hoy);
        cache.put(hoy, citasReales);
        return citasReales;
    }

    @Override
    public List<CitaResponseDTO> getAllCitas() {
        log.debug("PROXY CACHE: (Bypass) Delegando solicitud de 'todas las citas' al servicio real.");
        return realService.getAllCitas();
    }


    @Override
    public CitaDetailDTO getCitaDetailById(Long id) {
        log.debug("PROXY CACHE: (Bypass) Solicitando detalle clínico fresco al servicio real para Cita ID: {}", id);
        return realService.getCitaDetailById(id);
    }

    @Override
    public List<CitaDetailDTO> getAllCitasDetails() {
        log.debug("PROXY CACHE: (Bypass) Delegando solicitud de 'todas las citas detalladas' al servicio real.");
        return realService.getAllCitasDetails();
    }

    @Override
    public void confirmarCitaPorToken(String token) {
        log.debug("PROXY CACHE: (Write) Delegando confirmarCitaPorToken al servicio real...");
        realService.confirmarCitaPorToken(token);
        log.info("PROXY CACHE: (Invalidate 🗑️) Confirmación de cita detectada. Limpiando caché para {}.", LocalDate.now());
        cache.remove(LocalDate.now());
    }

    @Override
    public List<co.cue.citas_service.dtos.CitaResponseDTO> obtenerCitasFuturasPorVeterinario(Long veterinarioId) {
        log.debug("PROXY CACHE: (Bypass) Delegando solicitud de calendario completo al servicio real.");
        // No cacheamos el calendario completo ya que es una vista dinámica que cambia constantemente
        return realService.obtenerCitasFuturasPorVeterinario(veterinarioId);
    }

    @Override
    public List<co.cue.citas_service.dtos.CitaResponseDTO> obtenerTodasLasCitasPorVeterinario(Long veterinarioId) {
        log.debug("PROXY CACHE: (Bypass) Delegando solicitud de todas las citas al servicio real.");
        return realService.obtenerTodasLasCitasPorVeterinario(veterinarioId);
    }

    @Override
    public List<co.cue.citas_service.dtos.CitaResponseDTO> obtenerCitasPorVeterinarioYEstado(Long veterinarioId, String estado) {
        log.debug("PROXY CACHE: (Bypass) Delegando solicitud de citas por estado al servicio real.");
        return realService.obtenerCitasPorVeterinarioYEstado(veterinarioId, estado);
    }

    @Override
    public List<co.cue.citas_service.dtos.CitaResponseDTO> obtenerCitasFuturasPorVeterinarioYEstado(Long veterinarioId, String estado) {
        log.debug("PROXY CACHE: (Bypass) Delegando solicitud de citas futuras por estado al servicio real.");
        return realService.obtenerCitasFuturasPorVeterinarioYEstado(veterinarioId, estado);
    }

}
